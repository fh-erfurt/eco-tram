<template>
  <div id="lines" class="item-cards">
    <div class="title-frame">
      <div class="titles">
        <h1 class="title">Simulation <template v-if="ticks != null">(Ticks: {{ ticks }})</template></h1>
      </div>
      <div class="actions" v-if="hasStarted != null">
        <div class="action" v-if="!hasStarted" @click="startSimulation()">
          <i class="fa fa-play"></i>
          Simulation starten
        </div>
        <div class="action" v-else @click="stopSimulation()">
          <i class="fa fa-pause"></i>
          Simulation stoppen
        </div>
      </div>
    </div>
    <div v-if="hasStarted == null">Informationen werden geladen</div>
    <div v-else-if="!hasStarted">
      <div class="input-boxes">
        <div class="section">
          <h4 class="section-title">Allgemeine Einstellungen</h4>
          <div class="input-group">
            <div class="input-item">
              <label for="tickInterval">Tick alle ... Millisekunden</label>
              <input type="number" v-model="tickInterval" id="tickInterval">
            </div>
            <div class="input-item">
              <label for="dispatchInterval">Fortbewegungsintervall (in Ticks)</label>
              <input type="number" v-model="dispatchInterval" id="dispatchInterval">
            </div>
          </div>
          <div class="input-group">
            <div class="input-item">
              <label for="threadCount">Anzahl Threads</label>
              <input type="number" v-model="threadCount" id="threadCount">
            </div>
          </div>
        </div>
        <div v-if="loading">
          Linien werden geladen...
        </div>
        <template v-else>
          <div v-for="(line, index) in lines.results" :key="line.id" class="section">
            <h4 class="section-title">Einstellungen: <b>{{ line.name }}</b></h4>
            <div class="input-group">
              <div class="input-item">
                <label for="waitingTime">Wartezeit pro Straßenbahn (in Ticks)</label>
                <input type="number" v-model="lineSettings[index].waitingTime" id="waitingTime" min="0">
              </div>
              <div class="input-item">
                <label for="tramCount">Anzahl Straßenbahnen</label>
                <input type="number" v-model="lineSettings[index].tramCount" id="tramCount" max="60" min="1">
              </div>
            </div>
          </div>
        </template>
      </div>
    </div>
    <div v-else-if="lineSchedules == null">Linien werden geladen</div>
    <div v-else class="lineRow">
      <div class="column" v-for="(lineSchedule, index) in lineSchedules" :key="index">
        <h3>{{ lineSchedule.socketLine.name }}: {{ lineSchedule.entries.length }} Straßenbahnen</h3>
        <canvas ref="canvas"></canvas>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import {Component, Vue} from "vue-property-decorator";
import SockJS from 'sockjs-client'
import {Stomp} from '@stomp/stompjs'
import config from "@/config";
import {Line, PaginationResultType} from "@/types";

interface SocketPassengerTram {
  hash: string,
  currentIndex: number
  sourceStation: SocketStation
  destinationStation: SocketStation
}

interface SocketStation {
  hash: string
  name: string
}

interface SocketLine {
  name: string
  route: SocketStation[]
}

interface SocketLineScheduleEntry {
  hash: string
  startingTime: number
  startingOrder: number
  maxCount: number
  passengerTram?: SocketPassengerTram
}

interface SocketLineSchedule {
  socketLine: SocketLine
  entries: SocketLineScheduleEntry[]
}

enum FontAlignment {
  LEFT,
  CENTER,
  RIGHT
}

interface LineSettings {
  waitingTime: number
  tramCount: number
}

@Component
export default class Simulation extends Vue {

  private socket : any = null;
  private stompClient : any = null;
  private hasStarted : boolean | null = null;
  private lineSchedules: SocketLineSchedule[] | null = null
  private ticks: number | null = null;

  private loading = false
  private lines: PaginationResultType<Line> | null = null
  private lineSettings: LineSettings[] = []

  private tickInterval: number = 15
  private dispatchInterval: number = 15
  private threadCount: number = 1

  async mounted() {
    console.log("mount")
    if(this.socket) return;

    this.loadPreparedLines();

    this.socket = new SockJS(config.socketHost);
    this.stompClient = Stomp.over(this.socket);
    this.stompClient.debug = () => {};

    this.stompClient.connect({}, () => {
      this.stompClient.subscribe("/simulation/events/tram-start", (data: any) => {
        const body = JSON.parse(data.body);
        this.updateTram(body);
      });

      this.stompClient.subscribe("/simulation/events/status", (data: any) => {
        const body = JSON.parse(data.body);
        this.hasStarted = body.running;
        this.ticks = body.running ? body.ticks : null;
      })

      this.stompClient.subscribe("/simulation/events/start", (data: any) => {
        this.lineSchedules = null;
        this.hasStarted = true;
        this.loadLines();
      })

      this.stompClient.subscribe("/simulation/events/stop", (data: any) => {
        this.lineSchedules = null;
        this.ticks = null;
        this.hasStarted = false;
      })

      this.stompClient.subscribe("/user/simulation/status", (data: any) => {
         const body = JSON.parse(data.body);
         this.hasStarted = body.running;

         if(this.hasStarted)
           this.loadLines();
      })

      this.stompClient.send("/app/simulation/status", {});
    })
  }

  public async loadPreparedLines() {
    if(this.loading) return
    this.loading = true

    const response = await fetch(`${config.host}/lines/list?limit=1000`)
    const data: PaginationResultType<Line> = await response.json()

    const lineSettings: LineSettings[] = []

    for(const line of data.results) {
      lineSettings.push({ tramCount: 10, waitingTime: 20 })
    }

    this.lineSettings = lineSettings
    this.lines = data

    this.loading = false
  }

  loadLines() {
    console.log("loading lines");

    const lineSchedulesSubscription = this.stompClient.subscribe("/user/simulation/line-schedules", (data: any) => {
      this.lineSchedules = JSON.parse(data.body);
      this.stompClient.unsubscribe(lineSchedulesSubscription);

      try {
        for(let i = 0; i < this.lineSchedules!.length; i++) {
          const lineSchedule = this.lineSchedules![i]

          this.drawCanvas(i, lineSchedule)
        }
      } catch(e) {}
    })

    this.stompClient.send("/app/simulation/line-schedules", {});
  }

  updateTram(passengerTram: SocketPassengerTram) {
    if(!this.lineSchedules) return;

    const lineSchedules = this.lineSchedules!;

    for(const lineSchedule of lineSchedules)
      for(const entry of lineSchedule.entries) {
        if(entry.hash === passengerTram.hash)
          entry.passengerTram = passengerTram;
      }

    this.lineSchedules = [...lineSchedules];

    try {
      for(let i = 0; i < this.lineSchedules!.length; i++) {
        const lineSchedule = this.lineSchedules![i]

        this.drawCanvas(i, lineSchedule)
      }
    } catch(e) {

    }
  }

  startSimulation() {
    if(this.hasStarted) return;

    let outputLineSettings: { [id: number]: LineSettings } = {}

    for(let i = 0; i < this.lines!.results.length; i++) {
      const line = this.lines!.results[i]
      outputLineSettings[line.id] = this.lineSettings[i]
    }

    this.stompClient.send("/app/simulation/start", {}, JSON.stringify({
      tickInterval: this.tickInterval,
      threadCount: this.threadCount,
      dispatchInterval: this.dispatchInterval,
      settings: outputLineSettings
    }));
  }

  stopSimulation() {
    if(!this.hasStarted) return;
    this.stompClient.send("/app/simulation/stop", {});
  }

  drawCanvas(index: number, lineSchedule: SocketLineSchedule) {
    const refs: any = this.$refs['canvas'];

    if(!refs || !refs[index]) return

    const item: any = refs[index];

    const ctx: CanvasRenderingContext2D = item.getContext('2d')

    item.width = item.offsetWidth
    item.height = item.offsetHeight

    const {width, height} = item

    const verticalPadding = 10
    const horizontalPadding = 10
    const lineBarColor = '#ffffff'

    const stationColor = '#FFB900'
    const stationWidth = 10
    const stationHeight = 10
    const stationLabelColor = '#a7a7a7'
    const stationLabelFontSize = 8

    const passengerTramRadius = 3
    const passengerTramColor = '#00B7C3'
    const passengerTramMargin = 5
    const passengerTramAwaitingColor = '#a7a7a7'
    const passengerTramFontSize = 6

    function getRelativeToHeight(value: number) {
      return height / 100 * value
    }

    function drawText(text: string, font: string, fontWeight: number, fontSize: number, color: string, anchorX: number, anchorY: number, alignment: FontAlignment) {
      ctx.font = `${fontWeight} ${fontSize}px ${font}`
      ctx.fillStyle = color

      switch(alignment) {
        case FontAlignment.CENTER: {
          const textWidth = ctx.measureText(text).width
          ctx.fillText(text, anchorX - (textWidth / 2), anchorY)
        } break
        case FontAlignment.RIGHT: {
          const textWidth = ctx.measureText(text).width
          ctx.fillText(text, anchorX - textWidth, anchorY)
        } break
        default: {
          ctx.fillText(text, anchorX, anchorY)
        } break
      }
    }

    function getTextWidth(text: string, font: string, fontWeight: number, fontSize: number): number {
      ctx.font = `${fontWeight} ${fontSize}px ${font}`
      return ctx.measureText(text).width
    }

    ctx.clearRect(0, 0, width, height)

    ctx.fillStyle = '#393E46'
    ctx.fillRect(0, 0, width, height)

    function drawLineBar() {
      const relativeHorizontalPadding = Math.ceil(getRelativeToHeight(horizontalPadding))
      const relativeStationLabelFontSize = getRelativeToHeight(stationLabelFontSize)

      const barWidth = width - (relativeHorizontalPadding * 2)
      const barPosition = height / 2;

      const stations = lineSchedule.socketLine.route

      ctx.fillStyle = lineBarColor
      ctx.fillRect(relativeHorizontalPadding, barPosition - 1, barWidth, 2)

      const distance = barWidth / (stations.length - 1)


      const oddStations = stations.length % 2 === 1;

      for(let i = 0; i < stations.length; i++) {
        const station = stations[i]
        const centerX = relativeHorizontalPadding + (i * distance);
        const centerY = barPosition;

        ctx.save();

        ctx.translate(centerX, centerY)
        ctx.rotate(45 * Math.PI / 180)

        ctx.fillStyle = stationColor
        ctx.fillRect(-(stationWidth / 2), -(stationHeight / 2), stationWidth, stationHeight)

        ctx.restore()

        if(i === 0)
          drawText(station.name, 'Roboto, sans-serif', 400, relativeStationLabelFontSize, stationLabelColor, centerX - (stationWidth / 2), centerY + 30, FontAlignment.LEFT)
        else if(i === (stations.length - 1))
          drawText(station.name, 'Roboto, sans-serif', 400, relativeStationLabelFontSize, stationLabelColor, centerX + (stationWidth / 2), centerY + 30, FontAlignment.RIGHT)
        else if(oddStations && i === ((stations.length - 1) / 2))
          drawText(station.name, 'Roboto, sans-serif', 400, relativeStationLabelFontSize, stationLabelColor, centerX, centerY + 30, FontAlignment.CENTER)
      }
    }

    function drawPassengerTrams() {
      const relativeHorizontalPadding = getRelativeToHeight(horizontalPadding)
      const relativeVerticalPadding = getRelativeToHeight(verticalPadding)
      const relativePassengerTramRadius = getRelativeToHeight(passengerTramRadius)
      const relativePassengerTramMargin = getRelativeToHeight(passengerTramMargin)
      const relativePassengerTramFontSize = getRelativeToHeight(passengerTramFontSize)

      let awaitingPosition = 0;

      const barWidth = width - (relativeHorizontalPadding * 2)
      const barPosition = height / 2;

      const stations = lineSchedule.socketLine.route

      const distance = barWidth / (stations.length - 1)

      for(let i = 0; i < lineSchedule.entries.length; i++) {
        const entry = lineSchedule.entries[i]

        if(!entry.passengerTram) {
          ctx.fillStyle = passengerTramColor
          ctx.beginPath()
          ctx.arc(relativeHorizontalPadding + awaitingPosition, height - relativeVerticalPadding, relativePassengerTramRadius, 0, 2 * Math.PI)
          ctx.fill()

          ctx.fillStyle = passengerTramAwaitingColor

          drawText(entry.hash, 'Roboto, sans-serif', 400, relativePassengerTramFontSize, passengerTramAwaitingColor, relativeHorizontalPadding + awaitingPosition + (relativePassengerTramRadius * 2), height - relativeVerticalPadding + (relativePassengerTramFontSize / 2), FontAlignment.LEFT)

          const width = getTextWidth(entry.hash, 'Roboto, sans-serif', 400, relativePassengerTramFontSize)

          awaitingPosition += (relativePassengerTramRadius * 4) + relativePassengerTramMargin + width
        } else {

          ctx.fillStyle = passengerTramColor
          ctx.beginPath()
          ctx.arc(relativeHorizontalPadding + (distance * entry.passengerTram.currentIndex), barPosition, relativePassengerTramRadius, 0, 2 * Math.PI)
          ctx.fill()

          drawText(entry.passengerTram.hash, 'Roboto, sans-serif', 400, relativePassengerTramFontSize, passengerTramColor, relativeHorizontalPadding + (distance * entry.passengerTram.currentIndex), barPosition - 20, FontAlignment.CENTER)
        }
      }
    }

    drawLineBar()
    drawPassengerTrams()
  }
}
</script>

<style lang="scss" scoped>
.lineRow {
  display: flex;
  flex-direction: column;
  margin: -5px;

  .column {
    flex: 1;
    margin: 5px;
  }

  h3 {
    color: #a7a7a7;
  }
}

canvas {
  width: 100%;
  height: 200px;
  background: #393E46;
}
</style>
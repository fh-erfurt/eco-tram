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
    <div v-else-if="!hasStarted">Es läuft derzeit keine Simulation</div>
    <div v-else-if="lineSchedules == null">Linien werden geladen</div>
    <div v-else class="lineRow">
      <div class="column" v-for="(lineSchedule, index) in lineSchedules" :key="index">
        <h3>{{ lineSchedule.socketLine.name }}: {{ lineSchedule.entries.length }} Straßenbahnen</h3>
        <div v-for="(entry, index) in lineSchedule.entries" :key="entry.hash" class="item-card">
          <div class="name">
            <b>Straßenbahn {{ entry.hash }}</b>
            <span>
              Position: {{ entry.passengerTram ? entry.passengerTram.sourceStation.name : '-' }}
              <br>Ziel: {{ entry.passengerTram ? entry.passengerTram.destinationStation.name : '-' }}
            </span>
          </div>
          <div class="badges">
            <span class="badge warning" v-if="!entry.passengerTram">Wartet bis {{ entry.startingTime }} Ticks</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import SockJS from 'sockjs-client'
import {Stomp} from '@stomp/stompjs'

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

@Component
export default class Simulation extends Vue {

  private socket : any = null;
  private stompClient : any = null;
  private hasStarted : boolean | null = null;
  private lineSchedules: SocketLineSchedule[] | null = null
  private ticks: number | null = null;

  async mounted() {
    console.log("mount")
    if(this.socket) return;

    this.socket = new SockJS("http://localhost:8081/socket");
    this.stompClient = Stomp.over(this.socket);
    this.stompClient.debug = () => {};

    this.stompClient.connect({}, () => {
      // this.stompClient.subscribe("/simulation/events/start", (data: any) => {
      //   console.log(data.body);
      // });
      //
      //
      // this.stompClient.subscribe("/simulation/events/passengerTram-stop", (data: any) => {
      //   console.log(data.body);
      // });
      //
      // this.stompClient.subscribe("/simulation/events/start", (data: any) => {
      //   const body = JSON.parse(data.body);
      //   this.hasStarted = body.running;
      // })
      //

      this.stompClient.subscribe("/simulation/events/passengerTram-start", (data: any) => {
        const body = JSON.parse(data.body);
        this.updateTram(body);
        // console.log(data.body);
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

  loadLines() {
    console.log("loading lines");

    const lineSchedulesSubscription = this.stompClient.subscribe("/user/simulation/line-schedules", (data: any) => {
      this.lineSchedules = JSON.parse(data.body);
      this.stompClient.unsubscribe(lineSchedulesSubscription);
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
  }

  startSimulation() {
    if(this.hasStarted) return;
    this.stompClient.send("/app/simulation/start", {});
  }

  stopSimulation() {
    if(!this.hasStarted) return;
    this.stompClient.send("/app/simulation/stop", {});
  }
}
</script>

<style lang="scss">
.lineRow {
  display: flex;
  flex-wrap: wrap;
  align-items: flex-start;
  margin: -5px;

  .column {
    flex: 1;
    margin: 5px;
    min-width: 450px;
  }
}
</style>
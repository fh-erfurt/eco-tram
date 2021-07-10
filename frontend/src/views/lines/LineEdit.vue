<template>
    <div id="line-edit" class="item-cards">
        <simple-loader :data="line && stations" error-message="Die gesuchte Linie existiert nicht">
            <template v-if="stations && line && line.id">
                <div class="title-frame">
                    <div class="titles">
                        <h1 class="title">Linie, ID: {{ line.id }} bearbeiten</h1>
                    </div>
                </div>
                <div class="input-boxes">
                    <form class="section" @submit.prevent="submitGeneral">
                        <h4 class="section-title">Allgemeine Informationen</h4>
                        <div class="inner-section">
                            <div class="input-item">
                                <label for="name">Name</label>
                                <input type="text" v-model="name" id="name">
                            </div>
                            <div class="input-group">
                                <div class="input-item">
                                    <label>Verfügbare Stationen</label>
                                    <div class="select-box">
                                        <div class="item" v-for="station in stationsOrdered" :key="station.id" @click="addStation(station)">
                                            <i class="fas fa-h-square"></i> {{ station.name }} ({{ station.id }})
                                        </div>
                                    </div>
                                </div>
                                <div class="input-item">
                                    <label>Ausgewählte Stationen</label>
                                    <div class="select-box">
                                        <div class="item" v-for="(station, index) in selectedStations" :key="index" @click="removeStation(station)">
                                            <i class="fas fa-h-square"></i> {{ station.name }} ({{ station.id }})
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="submit-holder">
                                <button type="submit">
                                    <i class="fas fa-circle-notch fa-spin" v-if="submissions.general"></i>
                                    Speichern
                                </button>
                            </div>
                        </div>
                    </form>
                    <form class="section">
                        <h4 class="section-title">Weitere Aktionen</h4>
                        <div class="inner-section padding">
                            <a class="link" @click="deleteLine">
                                <i class="fas fa-circle-notch fa-spin" v-if="submissions.delete"></i>
                                Linie löschen
                            </a>
                        </div>
                    </form>
                </div>
            </template>
        </simple-loader>
    </div>
</template>

<script lang="ts">
import { Line, LineEntry, PaginationResultType, Station} from "@/types";
import { Component, Prop, Vue } from "vue-property-decorator";
import SimpleLoader from "@/components/SimpleLoader.vue"
import config from "@/config"

@Component({
    components: {
        SimpleLoader
    }
})
export default class LineEdit extends Vue {
    @Prop() private item?: Line | null = null

    private line: Line | null = this.item || null
    private lineCopy: Line | null = this.item || null

    private stations: Station[] | null = null

    private name: String = ""

    private selectedStations: Station[] = []

    private submissions = {
        general: false,
        delete: false
    }

    private addStation(station: Station) {
        this.selectedStations.push(station)
    }

    private removeStation(station: Station) {
        this.selectedStations.splice(this.selectedStations.indexOf(station), 1)
    }

    private async submitGeneral() {
        if(this.submissions.general) return
        this.submissions.general = true

        let stationIds = [];

        for(const station of this.selectedStations)
          stationIds.push(station.id)

        const response = await fetch(`${config.host}/lines/update/${this.$route.params.lineId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                name: this.name,
              stationIds: stationIds.join(',')
            })
        })

        const data: Line = await response.json()

        if(data.id) {
            this.line = data
            const lineCopy = this.lineCopy!

            lineCopy.name = data.name

            this.lineCopy  = {...lineCopy}
        }

        this.submissions.general = false
    }

    private async deleteLine() {
        if(this.submissions.delete || !confirm(`Möchtest Du die Linie ${ this.line!.id } wirklich löschen?`)) return
        this.submissions.delete = true

        await fetch(`${config.host}/lines/delete/${this.$route.params.lineId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
        })

        this.submissions.delete = false
        await this.$router.push({ name: 'lines' })
    }

    private changeLevelData(line: Line) {
        this.$root.$emit('change-level-data', 'lineView', {
            name: 'lineView',
            displayName: `Linie ID: ${line.id}`,
            params: {
                lineId: line.id,
                item: line
            }
        })
    }

    get routeOrdered() {
        function compare(lineEntry1: LineEntry, lineEntry2: LineEntry) {
            if (lineEntry1.orderValue > lineEntry2.orderValue)
                return 1;
            if (lineEntry2.orderValue > lineEntry1.orderValue)
                return -1;
            return 0;
        }

        return this.lineCopy!.route.sort(compare);
    }

    get stationsOrdered() {
        function compare(station1: Station, station2: Station) {
            if(station1.name < station2.name) return -1;
            if(station1.name > station2.name) return 1;
            return 0;
        }

        return this.stations!.sort(compare);
    }

    async mounted() {
        if(this.line) {
            this.changeLevelData(this.line)
            this.lineCopy = JSON.parse(JSON.stringify(this.line))

            this.name = this.line.name
        } else {
            const response = await fetch(`${config.host}/lines/get/${this.$route.params.lineId}`)
            this.line = await response.json()

            if(this.line && this.line.id) {
                this.changeLevelData(this.line)

                this.lineCopy = JSON.parse(JSON.stringify(this.line))

                this.name = this.line.name
            } else
                this.$root.$emit('change-level-name', 'lineView', 'Unbekannte Linie')
        }

        if(this.line && this.line.id) {
            const stationsResponse = await fetch(`${config.host}/stations/list?limit=0`)
            const stations = (await stationsResponse.json() as PaginationResultType<Station>).results

            for(const lineEntry of this.routeOrdered) {
                for (const station of stations) {
                    if (lineEntry.station.id === station.id) {
                        this.selectedStations.push(station)
                    }
                }
            }

            this.stations = [...stations]
        }
    }
}
</script>

<style lang="scss">
#line-edit {
    .select-box {
        background: white;
        border: solid 1px #e2e2e2;
        border-radius: 4px;
        min-height: 150px;
        width: 100%;
        color: black;
        padding: 5px 0;

        .item {
            display: flex;
            align-items: center;
            padding: 5px 10px;
            font-size: 13px;
            cursor: pointer;
            user-select: none;
            transition: background .2s ease;

            i {
                width: 40px;
                text-align: center;
            }

            &:hover {
                background: #f1f1f1;
            }
        }
    }
}
</style>
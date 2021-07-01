<template>
    <div id="line-edit" class="item-cards">
        <simple-loader :data="line && stations && connections" error-message="Die gesuchte Linie existiert nicht">
            <template v-if="stations && connections && line && line.id">
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
                                        <div class="item" v-for="station in stations" :key="station.id" @click="addStationToTraversableItems(station)">
                                            <i class="fa fa-sitemap"></i> {{ station.name }} ({{ station.id }})
                                        </div>
                                    </div>
                                </div>
                                <div class="input-item">
                                    <label>Verfügbare Linien</label>
                                    <div class="select-box">
                                        <div class="item" v-for="connection in connections" :key="connection.id" @click="addConnectionToTraversableItems(connection)">
                                            <i class="fas fa-h-square"></i> {{ connection.sourceStation.name }} - {{ connection.destinationStation.name }} ({{ connection.id }})
                                        </div>
                                    </div>
                                </div>
                                <div class="input-item">
                                    <label>Ausgewählte Streckenabschnitte</label>
                                    <div class="select-box">
                                        <div class="item" v-for="traversableItem in traversableItems" :key="traversableItem.id" @click="removeTraversableItem(traversableItem)">
                                            <template v-if="traversableItem.station">
                                                <i class="fa fa-sitemap"></i> {{ traversableItem.station.name }} ({{ traversableItem.station.id }})
                                            </template>
                                            <template v-else-if="traversableItem.connection">
                                                <i class="fas fa-h-square"></i> {{ traversableItem.connection.sourceStation.name }} - {{ traversableItem.connection.destinationStation.name }} ({{ traversableItem.connection.id }})
                                            </template>
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
import {Connection, Line, PaginationResultType, Station} from "@/types";
import { Component, Prop, Vue } from "vue-property-decorator";
import SimpleLoader from "@/components/SimpleLoader.vue"
import config from "@/config"

interface TraversableItem {
    id: number
    station?: Station
    connection?: Connection
}

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
    private connections: Connection[] | null = null

    private name: String = ""

    private traversableItems: TraversableItem[] = []

    private submissions = {
        general: false,
        delete: false
    }

    private addStationToTraversableItems(station: Station) {
        this.traversableItems.push({
            id: station.id,
            station: station
        })

        this.stations!.splice(this.stations!.indexOf(station), 1)
    }

    private addConnectionToTraversableItems(connection: Connection) {
        this.traversableItems.push({
            id: connection.id,
            connection: connection
        })

        this.connections!.splice(this.connections!.indexOf(connection), 1)
    }

    private removeTraversableItem(traversableItem: TraversableItem) {
        if(traversableItem.station) this.stations!.push(traversableItem.station)
        if(traversableItem.connection) this.connections!.push(traversableItem.connection)

        this.traversableItems.splice(this.traversableItems.indexOf(traversableItem), 1)
    }

    private async submitGeneral() {
        if(this.submissions.general) return
        this.submissions.general = true

        let traversableIds = [];

        for(const traversableItem of this.traversableItems)
            traversableIds.push(traversableItem.id)

        const response = await fetch(`${config.host}/lines/update/${this.$route.params.lineId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                name: this.name,
                traversableIds: traversableIds.join(',')
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

            const connectionsResponse = await fetch(`${config.host}/connections/list?limit=0`)
            const connections = (await connectionsResponse.json() as PaginationResultType<Connection>).results

            for(const traversable of this.line.route) {
                for (const station of stations) {
                    if (traversable.id === station.id) {
                        stations.splice(stations.indexOf(station), 1)
                        this.traversableItems.push({
                            id: station.id,
                            station: station
                        })
                    }
                }

                for (const connection of connections) {
                    if (traversable.id === connection.id) {
                        connections.splice(connections.indexOf(connection), 1)
                        this.traversableItems.push({
                            id: connection.id,
                            connection: connection
                        })
                    }
                }
            }

            this.stations = [...stations]
            this.connections = [...connections]
        }
    }
}
</script>
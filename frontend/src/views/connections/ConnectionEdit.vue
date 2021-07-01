<template>
    <div id="connection-edit" class="item-cards">
        <simple-loader :data="stations" error-message="Die gesuchte Verbindung existiert nicht">
            <template v-if="connection && connection.id">
                <div class="title-frame">
                    <div class="titles">
                        <h1 class="title">Verbindung, ID: {{ connection.id }} bearbeiten</h1>
                    </div>
                </div>
                <div class="input-boxes">
                    <form class="section" @submit.prevent="submitGeneral">
                        <h4 class="section-title">Allgemeine Informationen</h4>
                        <div class="inner-section">
                            <div class="input-group">
                                <div class="input-item">
                                    <label for="sourceStation">Ausgangsstation</label>
                                    <select v-model="sourceStation" id="sourceStation">
                                        <option :value="station" v-for="station in stations" :key="station.id">{{ `${ station.name } (ID: ${ station.id })` }}</option>
                                    </select>
                                </div>
                                <div class="input-item">
                                    <label for="destinationStation">Zielstation</label>
                                    <select v-model="destinationStation" id="destinationStation">
                                        <option :value="station" v-for="station in stations" :key="station.id">{{ `${ station.name } (ID: ${ station.id })` }}</option>
                                    </select>
                                </div>
                            </div>
                            <div class="input-group">
                                <div class="input-item">
                                    <label for="length">Länge</label>
                                    <input type="number" v-model="length" id="length">
                                </div>
                                <div class="input-item">
                                    <label for="trafficFactor">Traffic Factor</label>
                                    <input type="number" v-model="trafficFactor" id="trafficFactor">
                                </div>
                            </div>
                            <div class="input-item">
                                <label for="maxWeight">Maximales Gewicht</label>
                                <input type="number" v-model="maxWeight" id="maxWeight">
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
                            <a class="link" @click="deleteConnection">
                                <i class="fas fa-circle-notch fa-spin" v-if="submissions.delete"></i>
                                Verbindung löschen
                            </a>
                        </div>
                    </form>
                </div>
            </template>
        </simple-loader>
    </div>
</template>

<script lang="ts">
import { Connection, PaginationResultType, Station } from "@/types";
import { Component, Prop, Vue } from "vue-property-decorator";
import SimpleLoader from "@/components/SimpleLoader.vue"
import config from "@/config"

@Component({
    components: {
        SimpleLoader
    }
})
export default class ConnectionEdit extends Vue {
    @Prop() private item?: Connection | null = null

    private connection: Connection | null = this.item || null
    private connectionCopy: Connection | null = this.item || null

    private stations: Station[] | null = null

    private length: number = 0
    private maxWeight: number = 0
    private trafficFactor: number = 0.0
    private sourceStation: Station | null = null
    private destinationStation: Station | null = null

    private submissions = {
        general: false,
        delete: false
    }

    private async submitGeneral() {
        if(this.submissions.general) return
        this.submissions.general = true

        const response = await fetch(`${config.host}/connections/update/${this.$route.params.connectionId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                sourceStationId: this.sourceStation!.id,
                destinationStationId: this.destinationStation!.id,
                length: this.length,
                maxWeight: this.maxWeight,
                trafficFactor: this.trafficFactor
            })
        })

        const data: Connection = await response.json()

        if(data.id) {
            this.connection = data
            const connectionCopy = this.connectionCopy!

            connectionCopy.sourceStation = data.sourceStation
            connectionCopy.destinationStation = data.destinationStation
            connectionCopy.length = data.length
            connectionCopy.maxWeight = data.maxWeight
            connectionCopy.trafficFactor = data.trafficFactor

            this.connectionCopy  = {...connectionCopy}
        }

        this.submissions.general = false
    }

    private async deleteConnection() {
        if(this.submissions.delete || !confirm(`Möchtest Du die Verbindung ${ this.connection!.id } wirklich löschen?`)) return
        this.submissions.delete = true

        const response = await fetch(`${config.host}/connections/delete/${this.$route.params.connectionId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
        })

        this.submissions.delete = false
        await this.$router.push({ name: 'connections' })
    }

    private changeLevelData(connection: Connection) {
            this.$root.$emit('change-level-data', 'connectionView', {
                name: 'connectionView',
                displayName: `Verbindung ID: ${connection.id}`,
                params: {
                    connectionId: connection.id,
                    item: connection
                }
            })
    }

    async mounted() {
        if(this.connection) {
            this.changeLevelData(this.connection)
            this.connectionCopy = JSON.parse(JSON.stringify(this.connection))

            this.sourceStation = this.connection.sourceStation
            this.destinationStation = this.connection.destinationStation
            this.length = this.connection.length
            this.maxWeight = this.connection.maxWeight
            this.trafficFactor = this.connection.trafficFactor
        } else {
            const response = await fetch(`${config.host}/connections/get/${this.$route.params.connectionId}`)
            this.connection = await response.json()

            if(this.connection && this.connection.id) {
                this.changeLevelData(this.connection)
                
                this.connectionCopy = JSON.parse(JSON.stringify(this.connection))

                this.sourceStation = this.connection.sourceStation
                this.destinationStation = this.connection.destinationStation
                this.length = this.connection.length
                this.maxWeight = this.connection.maxWeight
                this.trafficFactor = this.connection.trafficFactor
            } else
                this.$root.$emit('change-level-name', 'connectionView', 'Unbekannte Verbindung')
        }

        if(this.connection && this.connection.id) {
            const response = await fetch(`${config.host}/stations/list`)
            this.stations = (await response.json() as PaginationResultType<Station>).results
        }
    }
}
</script>
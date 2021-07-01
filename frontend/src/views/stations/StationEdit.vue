<template>
    <div id="station-edit" class="item-cards">
        <simple-loader :data="station" error-message="Die gesuchte Station existiert nicht">
            <template v-if="station && station.id">
                <div class="title-frame">
                    <div class="titles">
                        <h1 class="title">Station, ID: {{ station.id }} bearbeiten</h1>
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
                                    <label for="length">Länge</label>
                                    <input type="number" v-model="length" id="length">
                                </div>
                                <div class="input-item">
                                    <label for="trafficFactor">Traffic Factor</label>
                                    <input type="number" v-model="trafficFactor" id="trafficFactor">
                                </div>
                            </div>
                            <div class="input-group">
                                <div class="input-item">
                                    <label for="maxWeight">Maximales Gewicht</label>
                                    <input type="number" v-model="maxWeight" id="maxWeight">
                                </div>
                                <div class="input-item">
                                    <label for="maxPassengers">Maximale Passagiere</label>
                                    <input type="number" v-model="maxPassengers" id="maxPassengers">
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
                            <a class="link" @click="deleteStation">
                                <i class="fas fa-circle-notch fa-spin" v-if="submissions.delete"></i>
                                Station löschen
                            </a>
                        </div>
                    </form>
                </div>
            </template>
        </simple-loader>
    </div>
</template>

<script lang="ts">
import { Station } from "@/types";
import { Component, Prop, Vue } from "vue-property-decorator";
import SimpleLoader from "@/components/SimpleLoader.vue"
import config from "@/config"

@Component({
    components: {
        SimpleLoader
    }
})
export default class StationEdit extends Vue {
    @Prop() private item?: Station | null = null

    private station: Station | null = this.item || null
    private stationCopy: Station | null = this.item || null

    private length: number = 0
    private maxWeight: number = 0
    private trafficFactor: number = 0.0
    private name: String = ""
    private maxPassengers: number = 0

    private submissions = {
        general: false,
        delete: false
    }

    private async submitGeneral() {
        if(this.submissions.general) return
        this.submissions.general = true

        const response = await fetch(`${config.host}/stations/update/${this.$route.params.stationId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                length: this.length,
                maxWeight: this.maxWeight,
                trafficFactor: this.trafficFactor,
                name: this.name,
                maxPassengers: this.maxPassengers
            })
        })

        const data: Station = await response.json()

        if(data.id) {
            this.station = data
            const stationCopy = this.stationCopy!

            stationCopy.length = data.length
            stationCopy.maxWeight = data.maxWeight
            stationCopy.trafficFactor = data.trafficFactor
            stationCopy.name = data.name
            stationCopy.maxPassengers = data.maxPassengers

            this.stationCopy  = {...stationCopy}
        }

        this.submissions.general = false
    }

    private async deleteStation() {
        if(this.submissions.delete || !confirm(`Möchtest Du die Station ${ this.station!.id } wirklich löschen?`)) return
        this.submissions.delete = true

        await fetch(`${config.host}/stations/delete/${this.$route.params.stationId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
        })

        this.submissions.delete = false
        await this.$router.push({ name: 'stations' })
    }

    private changeLevelData(station: Station) {
            this.$root.$emit('change-level-data', 'stationView', {
                name: 'stationView',
                displayName: `Station ID: ${station.id}`,
                params: {
                    stationId: station.id,
                    item: station
                }
            })
    }

    async mounted() {
        if(this.station) {
            this.changeLevelData(this.station)
            this.stationCopy = JSON.parse(JSON.stringify(this.station))

            this.length = this.station.length
            this.maxWeight = this.station.maxWeight
            this.trafficFactor = this.station.trafficFactor
            this.name = this.station.name
            this.maxPassengers = this.station.maxPassengers
        } else {
            const response = await fetch(`${config.host}/stations/get/${this.$route.params.stationId}`)
            this.station = await response.json()

            if(this.station && this.station.id) {
                this.changeLevelData(this.station)
                
                this.stationCopy = JSON.parse(JSON.stringify(this.station))

                this.length = this.station.length
                this.maxWeight = this.station.maxWeight
                this.trafficFactor = this.station.trafficFactor
                this.name = this.station.name
                this.maxPassengers = this.station.maxPassengers
            } else
                this.$root.$emit('change-level-name', 'stationView', 'Unbekannte Station')
        }
    }
}
</script>
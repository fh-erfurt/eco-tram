<template>
    <div id="passengerTram-edit" class="item-cards">
        <simple-loader :data="passengerTram" error-message="Die gesuchte Straßenbahn existiert nicht">
            <template v-if="passengerTram && passengerTram.id">
                <div class="title-frame">
                    <div class="titles">
                        <h1 class="title">Straßenbahn, ID: {{ passengerTram.id }} bearbeiten</h1>
                    </div>
                </div>
                <div class="input-boxes">
                    <form class="section" @submit.prevent="submitGeneral">
                        <h4 class="section-title">Allgemeine Informationen</h4>
                        <div class="inner-section">
                            <div class="input-item">
                                <label for="weight">Gewicht</label>
                                <input type="number" v-model="weight" id="weight">
                            </div>
                            <div class="input-group">
                                <div class="input-item">
                                    <label for="maxPassengers">Maximale Passagiere</label>
                                    <input type="number" v-model="maxPassengers" id="maxPassengers">
                                </div>
                                <div class="input-item">
                                    <label for="maxSpeed">Maximale Geschwindigkeit</label>
                                    <input type="number" v-model="maxSpeed" id="maxSpeed">
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
                            <a class="link" @click="deletePassengerTram">
                                <i class="fas fa-circle-notch fa-spin" v-if="submissions.delete"></i>
                                Straßenbahn löschen
                            </a>
                        </div>
                    </form>
                </div>
            </template>
        </simple-loader>
    </div>
</template>

<script lang="ts">
import { PassengerTram } from "@/types";
import { Component, Prop, Vue } from "vue-property-decorator";
import SimpleLoader from "@/components/SimpleLoader.vue"
import config from "@/config"

@Component({
    components: {
        SimpleLoader
    }
})
export default class PassengerTramEdit extends Vue {
    @Prop() private item?: PassengerTram | null = null

    private passengerTram: PassengerTram | null = this.item || null
    private passengerTramCopy: PassengerTram | null = this.item || null

    private maxPassengers: number = 0
    private weight: number = 0
    private maxSpeed: number = 0

    private submissions = {
        general: false,
        delete: false
    }

    private async submitGeneral() {
        if(this.submissions.general) return
        this.submissions.general = true

        const response = await fetch(`${config.host}/passenger-trams/update/${this.$route.params.passengerTramId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                maxPassengers: this.maxPassengers,
                weight: this.weight,
                maxSpeed: this.maxSpeed
            })
        })

        const data: PassengerTram = await response.json()

        if(data.id) {
            this.passengerTram = data
            const passengerTramCopy = this.passengerTramCopy!

            passengerTramCopy.weight = data.weight
            passengerTramCopy.maxSpeed = data.maxSpeed
            passengerTramCopy.maxPassengers = data.maxPassengers

            this.passengerTramCopy  = {...passengerTramCopy}
        }

        this.submissions.general = false
    }

    private async deletePassengerTram() {
        if(this.submissions.delete || !confirm(`Möchtest Du die Straßenbahn ${ this.passengerTram!.id } wirklich löschen?`)) return
        this.submissions.delete = true

        await fetch(`${config.host}/passenger-trams/delete/${this.$route.params.passengerTramId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
        })

        this.submissions.delete = false
        await this.$router.push({ name: 'passengerTrams' })
    }

    private changeLevelData(passengerTram: PassengerTram) {
            this.$root.$emit('change-level-data', 'passengerTramView', {
                name: 'passengerTramView',
                displayName: `Straßenbahn ID: ${passengerTram.id}`,
                params: {
                    passengerTramId: passengerTram.id,
                    item: passengerTram
                }
            })
    }

    async mounted() {
        if(this.passengerTram) {
            this.changeLevelData(this.passengerTram)
            this.passengerTramCopy = JSON.parse(JSON.stringify(this.passengerTram))

            this.weight = this.passengerTram.weight
            this.maxSpeed = this.passengerTram.maxSpeed
            this.maxPassengers = this.passengerTram.maxPassengers
        } else {
            const response = await fetch(`${config.host}/passenger-trams/get/${this.$route.params.passengerTramId}`)
            this.passengerTram = await response.json()

            if(this.passengerTram && this.passengerTram.id) {
                this.changeLevelData(this.passengerTram)
                
                this.passengerTramCopy = JSON.parse(JSON.stringify(this.passengerTram))

                this.weight = this.passengerTram.weight
                this.maxSpeed = this.passengerTram.maxSpeed
                this.maxPassengers = this.passengerTram.maxPassengers
            } else
                this.$root.$emit('change-level-name', 'passengerTramView', 'Unbekannte Straßenbahn')
        }
    }
}
</script>
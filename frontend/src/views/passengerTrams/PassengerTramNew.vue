<template>
    <div id="passenger-tram-new" class="item-cards">
        <div class="title-frame">
            <div class="titles">
                <h1 class="title">Straßenbahn erstellen</h1>
            </div>
        </div>
        <div class="input-boxes">
            <form class="section" @submit.prevent="submitGeneral">
                <h4 class="section-title">Neue Straßenbahn</h4>
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
                            <i class="fas fa-circle-notch fa-spin" v-if="submitting"></i>
                            Speichern
                        </button>
                    </div>
                </div>
            </form>
        </div>
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
export default class PassengerTramNew extends Vue {
    @Prop() private item?: PassengerTram | null = null

    private maxPassengers: number = 0
    private weight: number = 0
    private maxSpeed: number = 0

    private submitting: boolean = false

    private async submitGeneral() {
        if(this.submitting) return
        this.submitting = true

        const response = await fetch(`${config.host}/passenger-trams/new`, {
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

        if(data.id)
            await this.$router.push({ name: 'passengerTramView', params: { passengerTramId: `${data.id}` } })
    }
}
</script>
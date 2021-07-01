<template>
    <div id="connection-new" class="item-cards">
        <simple-loader :data="stations" error-message="Ein Fehler ist aufgetreten">
            <template v-if="stations">
                <div class="title-frame">
                    <div class="titles">
                        <h1 class="title">Verbindung erstellen</h1>
                    </div>
                </div>
                <div class="input-boxes">
                    <form class="section" @submit.prevent="submitGeneral">
                        <h4 class="section-title">Neue Verbindung</h4>
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
export default class ConnectionNew extends Vue {
    @Prop() private item?: Connection | null = null

    private stations: Station[] | null = null

    private length: number = 0
    private maxWeight: number = 0
    private trafficFactor: number = 0.0
    private sourceStation: Station | null = null
    private destinationStation: Station | null = null

    private submitting: boolean = false

    private async submitGeneral() {
        if(this.submitting) return
        this.submitting = true

        const response = await fetch(`${config.host}/connections/new`, {
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

        if(data.id)
            await this.$router.push({ name: 'connectionView', params: { connectionId: `${data.id}` } })
    }

    async mounted() {
        const response = await fetch(`${config.host}/stations/list`)
        this.stations = (await response.json() as PaginationResultType<Station>).results
    }
}
</script>
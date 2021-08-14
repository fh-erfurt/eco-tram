<template>
    <div id="station-new" class="item-cards">
        <div class="title-frame">
            <div class="titles">
                <h1 class="title">Station erstellen</h1>
            </div>
        </div>
        <div class="input-boxes">
            <form class="section" @submit.prevent="submitGeneral">
                <h4 class="section-title">Neue Station</h4>
                <div class="inner-section">
                    <div class="input-group">
                        <div class="input-item">
                            <label for="name">Name</label>
                            <input type="text" v-model="name" id="name">
                        </div>
                        <div class="input-item">
                            <label for="maxPassengers">Maximale Passagiere</label>
                            <input type="number" v-model="maxPassengers" id="maxPassengers">
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
import { Station } from "@/types";
import { Component, Prop, Vue } from "vue-property-decorator";
import SimpleLoader from "@/components/SimpleLoader.vue"
import config from "@/config"

@Component({
    components: {
        SimpleLoader
    }
})
export default class StationNew extends Vue {
    @Prop() private item?: Station | null = null

    private name: String = ""
    private maxPassengers: number = 0

    private submitting: boolean = false

    private async submitGeneral() {
        if(this.submitting) return
        this.submitting = true

        const response = await fetch(`${config.host}/stations/new`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                name: this.name,
                maxPassengers: this.maxPassengers
            })
        })

        const data: Station = await response.json()

        if(data.id)
            await this.$router.push({ name: 'stationView', params: { stationId: `${data.id}` } })
    }
}
</script>
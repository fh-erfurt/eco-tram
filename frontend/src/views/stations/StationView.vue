<template>
    <div id="station-view" class="item-cards">
        <simple-loader :data="station" error-message="Die gesuchte Station existiert nicht">
            <template v-if="station && station.id">
                <div class="title-frame">
                    <div class="titles">
                        <h1 class="title">{{station.name }}</h1>
                        <h3 class="subtitle">ID: {{ station.id }}</h3>
                    </div>
                    <div class="actions">
                        <router-link :to="{ name: 'stationEdit', params: { stationId: station.id, item: station } }" class="action">
                            <i class="fa fa-pencil-alt"></i>
                            Bearbeiten
                        </router-link>
                    </div>
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
export default class stationView extends Vue {
    @Prop() private item?: Station | null = null

    private station: Station | null = this.item || null

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
        if(this.station)
            this.changeLevelData(this.station)
        else {
            const response = await fetch(`${config.host}/stations/get/${this.$route.params.stationId}`)
            this.station = await response.json()

            if(this.station && this.station.id)
                this.changeLevelData(this.station)
            else
                this.$root.$emit('change-level-name', 'stationView', 'Unbekannte Linie')
        }

    }
}
</script>
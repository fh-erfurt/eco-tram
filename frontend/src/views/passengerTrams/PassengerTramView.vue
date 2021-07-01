<template>
    <div id="passengerTram-view" class="item-cards">
        <simple-loader :data="passengerTram" error-message="Die gesuchte Straßenbahn existiert nicht">
            <template v-if="passengerTram && passengerTram.id">
                <div class="title-frame">
                    <div class="titles">
                        <h1 class="title">Straßenbahn, ID: {{passengerTram.id }}</h1>
                    </div>
                    <div class="actions">
                        <router-link :to="{ name: 'passengerTramEdit', params: { passengerTramId: passengerTram.id, item: passengerTram } }" class="action">
                            <i class="fa fa-pencil-alt"></i>
                            Bearbeiten
                        </router-link>
                    </div>
                </div>
                <div class="title-badges">
                    <span class="badge good">W: {{ passengerTram.weight }}</span>
                    <span class="badge good">MS: {{ passengerTram.maxSpeed }}</span>
                    <span class="badge good">MP: {{ passengerTram.maxPassengers }}</span>
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
export default class passengerTramView extends Vue {
    @Prop() private item?: PassengerTram | null = null

    private passengerTram: PassengerTram | null = this.item || null

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
        if(this.passengerTram)
            this.changeLevelData(this.passengerTram)
        else {
            const response = await fetch(`${config.host}/passenger-trams/get/${this.$route.params.passengerTramId}`)
            this.passengerTram = await response.json()

            if(this.passengerTram && this.passengerTram.id)
                this.changeLevelData(this.passengerTram)
            else
                this.$root.$emit('change-level-name', 'passengerTramView', 'Unbekannte Straßenbahn')
        }

    }
}
</script>
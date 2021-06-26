<template>
    <div id="connection-view" class="item-cards">
        <simple-loader :data="connection" error-message="Die gesuchte Verbindung existiert nicht">
            <template v-if="connection && connection.id">
                <div class="title-frame">
                    <div class="titles">
                        <h1 class="title">Verbindung, ID: {{ connection.id }}</h1>
                        <h3 class="subtitle">{{ `${ connection.sourceStation.name } - ${ connection.destinationStation.name }` }}</h3>
                    </div>
                    <div class="actions">
                        <router-link :to="{ name: 'connectionEdit', params: { connectionId: connection.id, item: connection } }" class="action">
                            <i class="fa fa-pencil-alt"></i>
                            Bearbeiten
                        </router-link>
                    </div>
                </div>
                <div class="title-badges">
                    <span class="badge good">L: {{ connection.length }}</span>
                    <span class="badge good">TF: {{ connection.trafficFactor }}</span>
                    <span class="badge good">MW: {{ connection.maxWeight }}</span>
                </div>
                <simple-loader :data="fullyLoaded ? connection : null" error-message="Ein Fehler ist aufgetreten">
                    <template v-if="fullyLoaded && connection && connection.id">
                        <h3 class="title small">Ausgangsstation</h3>
                        <router-link :to="{ name: 'stationView', params: { stationId: connection.sourceStation.id, item: connection.sourceStation } }" class="item-card">
                            <div class="name">
                                <b>{{ connection.sourceStation.name }}</b>
                                <span>ID: {{ connection.sourceStation.id }}</span>
                            </div>
                            <div class="badges">
                                <span class="badge good">L: {{ connection.sourceStation.length }}</span>
                                <span class="badge good">TF: {{ connection.sourceStation.trafficFactor }}</span>
                                <span class="badge good">MW: {{ connection.sourceStation.maxWeight }}</span>
                                <span class="badge good">MP: {{ connection.sourceStation.maxPassengers }}</span>
                                <span class="badge good">CP: {{ connection.sourceStation.currentPassengers }}</span>
                            </div>
                        </router-link>
                        <h3 class="title small">Zielstation</h3>
                        <router-link :to="{ name: 'stationView', params: { stationId: connection.destinationStation.id, item: connection.destinationStation } }" class="item-card">
                            <div class="name">
                                <b>{{ connection.destinationStation.name }}</b>
                                <span>ID: {{ connection.destinationStation.id }}</span>
                            </div>
                            <div class="badges">
                                <span class="badge good">L: {{ connection.destinationStation.length }}</span>
                                <span class="badge good">TF: {{ connection.destinationStation.trafficFactor }}</span>
                                <span class="badge good">MW: {{ connection.destinationStation.maxWeight }}</span>
                                <span class="badge good">MP: {{ connection.destinationStation.maxPassengers }}</span>
                                <span class="badge good">CP: {{ connection.destinationStation.currentPassengers }}</span>
                            </div>
                        </router-link>
                    </template>
                </simple-loader>
            </template>
        </simple-loader>
    </div>
</template>

<script lang="ts">
import { Connection } from "@/types";
import { Component, Prop, Vue } from "vue-property-decorator";
import SimpleLoader from "@/components/SimpleLoader.vue"
import config from "@/config"

@Component({
    components: {
        SimpleLoader
    }
})
export default class ConnectionView extends Vue {
    @Prop() private item?: Connection | null = null

    private connection: Connection | null = this.item || null
    private fullyLoaded: boolean = !this.item

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
        if(this.connection)
        this.changeLevelData(this.connection)

        const response = await fetch(`${config.host}/connections/get/${this.$route.params.connectionId}`)
        this.connection = await response.json()
        this.fullyLoaded = true

        if(this.connection && this.connection.id)
            this.changeLevelData(this.connection)
        else
            this.$root.$emit('change-level-name', 'connectionView', 'Unbekannte Verbindung')
    }
}
</script>
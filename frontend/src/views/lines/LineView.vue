<template>
    <div id="line-view" class="item-cards">
        <simple-loader :data="line" error-message="Die gesuchte Linie existiert nicht">
            <template v-if="line && line.id">
                <div class="title-frame">
                    <div class="titles">
                        <h1 class="title">{{line.name }}</h1>
                        <h3 class="subtitle">ID: {{ line.id }}</h3>
                    </div>
                    <div class="actions">
                        <router-link :to="{ name: 'lineEdit', params: { lineId: line.id, item: line } }" class="action">
                            <i class="fa fa-pencil-alt"></i>
                            Bearbeiten
                        </router-link>
                    </div>
                </div>
                <h3 class="title small">Route</h3>
                <router-link v-for="item in line.route" :key="item.id" :to="item.sourceStation ? { name: 'connectionView', params: { connectionId: item.id, item: item } } : { name: 'stationView', params: { stationId: item.id, item: item } }" class="item-card">
                    <template v-if="item.sourceStation">
                        <div class="name">
                            <b>Verbindung, ID: {{ item.id }}</b>
                            <span>{{ `${ item.sourceStation.name } - ${ item.destinationStation.name }` }}</span>
                        </div>
                        <div class="badges">
                            <span class="badge good">L: {{ item.length }}</span>
                            <span class="badge good">TF: {{ item.trafficFactor }}</span>
                            <span class="badge good">MW: {{ item.maxWeight }}</span>
                        </div>
                    </template>
                    <template v-else>
                        <div class="name">
                            <b>{{item.name }}</b>
                            <span>ID: {{ item.id }}</span>
                        </div>
                    </template>
                </router-link>
            </template>
        </simple-loader>
    </div>
</template>

<script lang="ts">
import { Line } from "@/types";
import { Component, Prop, Vue } from "vue-property-decorator";
import SimpleLoader from "@/components/SimpleLoader.vue"
import config from "@/config"

@Component({
    components: {
        SimpleLoader
    }
})
export default class lineView extends Vue {
    @Prop() private item?: Line | null = null

    private line: Line | null = this.item || null

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
        if(this.line)
            this.changeLevelData(this.line)
        else {
            const response = await fetch(`${config.host}/lines/get/${this.$route.params.lineId}`)
            this.line = await response.json()

            if(this.line && this.line.id)
                this.changeLevelData(this.line)
            else
                this.$root.$emit('change-level-name', 'lineView', 'Unbekannte Linie')
        }

    }
}
</script>
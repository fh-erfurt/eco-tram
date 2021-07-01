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
                <router-link v-for="item in routeOrdered" :key="item.id" :to="item.traversable.sourceStation ? { name: 'connectionView', params: { connectionId: item.traversable.id, item: item.traversable } } : { name: 'stationView', params: { stationId: item.traversable.id, item: item.traversable } }" class="item-card">
                    <template v-if="item.traversable.sourceStation">
                        <div class="image"><i class="fa fa-sitemap"></i></div>
                        <div class="name">
                            <b>Verbindung, ID: {{ item.traversable.id }}</b>
                            <span>{{ `${ item.traversable.sourceStation.name } - ${ item.traversable.destinationStation.name }` }}</span>
                        </div>
                        <div class="badges">
                            <span class="badge good">L: {{ item.traversable.length }}</span>
                            <span class="badge good">TF: {{ item.traversable.trafficFactor }}</span>
                            <span class="badge good">MW: {{ item.traversable.maxWeight }}</span>
                        </div>
                    </template>
                    <template v-else>
                        <div class="image"><i class="fas fa-h-square"></i></div>
                        <div class="name">
                            <b>{{item.traversable.name }}</b>
                            <span>ID: {{ item.traversable.id }}</span>
                        </div>
                    </template>
                </router-link>
            </template>
        </simple-loader>
    </div>
</template>

<script lang="ts">
import {Line, LineEntry} from "@/types";
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

    get routeOrdered() {
        function compare(lineEntry1: LineEntry, lineEntry2: LineEntry) {
            if (lineEntry1.orderValue > lineEntry2.orderValue)
                return 1;
            if (lineEntry2.orderValue > lineEntry1.orderValue)
                return -1;
            return 0;
        }

        return this.line!.route.sort(compare);
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
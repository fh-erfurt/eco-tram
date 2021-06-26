<template>
    <div id="connections" class="item-cards">
        <div class="title-frame">
            <div class="titles">
                <h1 class="title">Verbindungen <template v-if="connections">({{ connections.total }})</template></h1>
            </div>
            <div class="actions">
                <router-link :to="{ name: 'connectionNew' }" class="action">
                    <i class="fa fa-plus"></i>
                    Neue Verbindung
                </router-link>
            </div>
        </div>
        <pagination-result
            :data="connections"
            :loading="loading"
            emptyMessage="Keine Verbindungen gefunden"
            @loadMore="loadConnections"
        >
            <template v-if="connections">
                <router-link :to="{ name: 'connectionView', params: { connectionId: connection.id, item: connection } }" class="item-card" v-for="connection in connections.results" :key="connection.id">
                    <div class="name">
                        <b>Verbindung, ID: {{ connection.id }}</b>
                        <span>{{ `${ connection.sourceStation.name } - ${ connection.destinationStation.name }` }}</span>
                    </div>
                    <div class="badges">
                        <span class="badge good">L: {{ connection.length }}</span>
                        <span class="badge good">TF: {{ connection.trafficFactor }}</span>
                        <span class="badge good">MW: {{ connection.maxWeight }}</span>
                    </div>
                </router-link>
            </template>
        </pagination-result>
    </div>
</template>

<script lang="ts">
import { Connection, PaginationResultType } from "@/types";
import { Component, Vue } from "vue-property-decorator";
import PaginationResult from "@/components/PaginationResult.vue"
import config from "@/config"

@Component({
    components: {
        PaginationResult
    }
})
export default class Connections extends Vue {
    private connections: PaginationResultType<Connection> | null = null
    private loading = false

    public async loadConnections(limit = 50, page = 0, query = '') {
        if(this.loading) return
        this.loading = true

        if(page === 0) this.connections = null

        const response = await fetch(`${config.host}/connections/list?limit=${limit}&page=${page}&query=${query}`)
        const data: PaginationResultType<Connection> = await response.json()

        if(this.connections == null) this.connections = data
        else {
            data.results = [...this.connections.results, ...data.results]
            data.count = this.connections.count + data.count

            this.connections = {...data}
        }

        this.loading = false
    }

    async mounted() {
        await this.loadConnections()
    }
}
</script>
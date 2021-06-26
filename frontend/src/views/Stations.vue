<template>
  <div id="stations" class="item-cards">
    <div class="title-frame">
      <div class="titles">
        <h1 class="title">Stationen<template v-if="stations"> ({{ stations.total }})</template></h1>
      </div>
      <div class="actions">
        <router-link :to="{ name: 'stationNew' }" class="action">
          <i class="fa fa-plus"></i>
          neue Station
        </router-link>
      </div>
    </div>
    <pagination-result
        :data="stations"
        :loading="loading"
        emptyMessage="Keine Stationen gefunden"
        @loadMore="loadStations"
    >
      <template v-if="stations">
        <router-link :to="{ name: 'stationView', params: { stationId: station.id, item: station } }" class="item-card" v-for="station in stations.results" :key="station.id">
          <div class="name">
            <b>{{statione.name }}</b>
            <span>{{ station.id }}</span>
          </div>
        </router-link>
      </template>
    </pagination-result>
  </div>
</template>

<script lang="ts">
import { Station, PaginationResultType } from "@/types";
import { Component, Vue } from "vue-property-decorator";
import PaginationResult from "@/components/PaginationResult.vue"
import config from "@/config"

@Component({
  components: {
    PaginationResult
  }
})
export default class Stations extends Vue {
  private stations: PaginationResultType<Station> | null = null
  private loading = false

  public async loadStations(limit = 50, page = 0, query = '') {
    if(this.loading) return
    this.loading = true

    if(page === 0) this.stations = null

    const response = await fetch(`${config.host}/stations/list?limit=${limit}&page=${page}&query=${query}`)
    const data: PaginationResultType<Station> = await response.json()

    if(this.stations == null) this.stations = data
    else {
      data.results = [...this.stations.results, ...data.results]
      data.count = this.stations.count + data.count

      this.stations = {...data}
    }

    this.loading = false
  }

  async mounted() {
    await this.loadStations()
  }
}
</script>
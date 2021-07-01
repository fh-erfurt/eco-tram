<template>
  <div id="passengerTrams" class="item-cards">
    <div class="title-frame">
      <div class="titles">
        <h1 class="title">Straßenbahnen<template v-if="passengerTrams"> ({{ passengerTrams.total }})</template></h1>
      </div>
      <div class="actions">
        <router-link :to="{ name: 'passengerTramNew' }" class="action">
          <i class="fa fa-plus"></i>
          Neue Straßenbahn
        </router-link>
      </div>
    </div>
    <pagination-result
        :data="passengerTrams"
        :loading="loading"
        emptyMessage="Keine Straßenbahnen gefunden"
        @loadMore="loadPassengerTrams"
    >
      <template v-if="passengerTrams">
        <router-link :to="{ name: 'passengerTramView', params: { passengerTramId: passengerTram.id, item: passengerTram } }" class="item-card" v-for="passengerTram in passengerTrams.results" :key="passengerTram.id">
          <div class="name">
            <b>Straßenbahn, ID: {{ passengerTram.id }}</b>
          </div>
          <div class="badges">
            <span class="badge good">W: {{ passengerTram.weight }}</span>
            <span class="badge good">MS: {{ passengerTram.maxSpeed }}</span>
            <span class="badge good">MP: {{ passengerTram.maxPassengers }}</span>
          </div>
        </router-link>
      </template>
    </pagination-result>
  </div>
</template>

<script lang="ts">
import { PassengerTram, PaginationResultType } from "@/types";
import { Component, Vue } from "vue-property-decorator";
import PaginationResult from "@/components/PaginationResult.vue"
import config from "@/config"

@Component({
  components: {
    PaginationResult
  }
})
export default class PassengerTrams extends Vue {
  private passengerTrams: PaginationResultType<PassengerTram> | null = null
  private loading = false

  public async loadPassengerTrams(limit = 50, page = 0, query = '') {
    if(this.loading) return
    this.loading = true

    if(page === 0) this.passengerTrams = null

    const response = await fetch(`${config.host}/passenger-trams/list?limit=${limit}&page=${page}&query=${query}`)
    const data: PaginationResultType<PassengerTram> = await response.json()

    if(this.passengerTrams == null) this.passengerTrams = data
    else {
      data.results = [...this.passengerTrams.results, ...data.results]
      data.count = this.passengerTrams.count + data.count

      this.passengerTrams = {...data}
    }

    this.loading = false
  }

  async mounted() {
    await this.loadPassengerTrams()
  }
}
</script>
<template>
  <div id="lines" class="item-cards">
    <div class="title-frame">
      <div class="titles">
        <h1 class="title">Linien<template v-if="lines"> ({{ lines.total }})</template></h1>
      </div>
      <div class="actions">
        <router-link :to="{ name: 'lineNew' }" class="action">
          <i class="fa fa-plus"></i>
          Neue Linie
        </router-link>
      </div>
    </div>
    <pagination-result
        :data="lines"
        :loading="loading"
        emptyMessage="Keine Linien gefunden"
        @loadMore="loadLines"
    >
      <template v-if="lines">
        <router-link :to="{ name: 'lineView', params: { lineId: line.id, item: line } }" class="item-card" v-for="line in lines.results" :key="line.id">
          <div class="name">
              <b>{{ line.name }}</b>
              <span>ID: {{ line.id }}</span>
          </div>
        </router-link>
      </template>
    </pagination-result>
  </div>
</template>

<script lang="ts">
import { Line, PaginationResultType } from "@/types";
import { Component, Vue } from "vue-property-decorator";
import PaginationResult from "@/components/PaginationResult.vue"
import config from "@/config"

@Component({
  components: {
    PaginationResult
  }
})
export default class Lines extends Vue {
  private lines: PaginationResultType<Line> | null = null
  private loading = false

  public async loadLines(limit = 50, page = 0, query = '') {
    if(this.loading) return
    this.loading = true

    if(page === 0) this.lines = null

    const response = await fetch(`${config.host}/lines/list?limit=${limit}&page=${page}&query=${query}`)
    const data: PaginationResultType<Line> = await response.json()

    if(this.lines == null) this.lines = data
    else {
      data.results = [...this.lines.results, ...data.results]
      data.count = this.lines.count + data.count

      this.lines = {...data}
    }

    this.loading = false
  }

  async mounted() {
    await this.loadLines()
  }
}
</script>
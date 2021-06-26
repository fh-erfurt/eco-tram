<template>
  <div id="home">
    <h1 class="title">Eco Tram Administration</h1>
    <div class="options">
      <router-link :to="{ name: 'connections' }" class="option">
        <div class="name">
          <i class="fas fa-sitemap"></i>
          Verbindungen
        </div>
        <div class="count" v-if="statistics == null"><i class="fas fa-circle-notch fa-spin"></i></div>
        <div class="count" v-else>{{ statistics.connections }}</div>
      </router-link>
      <router-link :to="{ name: 'lines' }" class="option">
        <div class="name">
          <i class="fas fa-code-branch"></i>
          Linien
        </div>
        <div class="count" v-if="statistics == null"><i class="fas fa-circle-notch fa-spin"></i></div>
        <div class="count" v-else>{{ statistics.lines.results }}</div>
      </router-link>
      <router-link :to="{ name: 'passengerTrams' }" class="option">
        <div class="name">
          <i class="fa fa-subway"></i>
          Straßenbahnen
        </div>
        <div class="count" v-if="statistics == null"><i class="fas fa-circle-notch fa-spin"></i></div>
        <div class="count" v-else>{{ statistics.passengerTrams.results }}</div>
      </router-link>
      <router-link :to="{ name: 'stations' }" class="option">
        <div class="name">
          <i class="fas fa-h-square"></i>
          Stationen
        </div>
        <div class="count" v-if="statistics == null"><i class="fas fa-circle-notch fa-spin"></i></div>
        <div class="count" v-else>{{ statistics.stations.results }}</div>
      </router-link>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

interface Statistics {
  connections: number;
}

@Component
export default class Home extends Vue {
  private statistics?: Statistics;

  async mounted() {
    const response = await fetch(`${config.host}/statistics`)
    this.statistics = await response.json()
  }
}
</script>

<style lang="scss" scoped>
.options {
  margin: 10px 0;

  .option {
    background: white;
    border: solid 1px #e2e2e2;
    border-radius: 4px;
    display: flex;
    height: 60px;
    padding: 8px;
    align-items: center;
    text-decoration: none;
    color: #3c3c3c;
    transition: background 0.2s ease;

    &:hover {
      background: #f1f1f1;
    }

    .name {
      flex: 1;
      display: flex;
      align-items: center;
      font-weight: 500;

      i {
        width: 40px;
        text-align: center;
        font-size: 20px;
        color: #216383;
        margin-right: 10px;
      }
    }

    .count {
      padding: 0 10px;
    }

    & + .option {
      margin-top: 10px;
    }
  }
}
</style>
<template>
    <div id="line-new" class="item-cards">
        <simple-loader :data="stations" error-message="Ein Fehler ist aufgetreten">
            <template v-if="stations">
                <div class="title-frame">
                    <div class="titles">
                        <h1 class="title">Linie erstellen</h1>
                    </div>
                </div>
                <div class="input-boxes">
                    <form class="section" @submit.prevent="submitGeneral">
                        <h4 class="section-title">Neue Line</h4>
                        <div class="inner-section">
                            <div class="input-item">
                                <label for="name">Name</label>
                                <input type="text" v-model="name" id="name">
                            </div>
                            <div class="input-group">
                                <div class="input-item">
                                    <label>Verfügbare Stationen</label>
                                    <div class="select-box">
                                        <div class="item" v-for="station in stationsOrdered" :key="station.id" @click="addStation(station)">
                                            <i class="fas fa-h-square"></i> {{ station.name }} ({{ station.id }})
                                        </div>
                                    </div>
                                </div>
                                <div class="input-item">
                                    <label>Ausgewählte Stationen</label>
                                    <div class="select-box">
                                        <div class="item" v-for="(station, index) in selectedStations" :key="index" @click="removeStation(station)">
                                          <i class="fas fa-h-square"></i> {{ station.name }} ({{ station.id }})
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="submit-holder">
                                <button type="submit">
                                    <i class="fas fa-circle-notch fa-spin" v-if="submitting"></i>
                                    Speichern
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </template>
        </simple-loader>
    </div>
</template>

<script lang="ts">
import { Line, PaginationResultType, Station } from "@/types";
import { Component, Prop, Vue } from "vue-property-decorator";
import SimpleLoader from "@/components/SimpleLoader.vue"
import config from "@/config"


@Component({
    components: {
        SimpleLoader
    }
})
export default class LineNew extends Vue {
    @Prop() private item?: Line | null = null

    private stations: Station[] | null = null

    private name: String = ""

    private submitting: boolean = false

    private selectedStations: Station[] = []

    get stationsOrdered() {
        function compare(station1: Station, station2: Station) {
            if(station1.name < station2.name) return -1;
            if(station1.name > station2.name) return 1;
            return 0;
        }

        return this.stations!.sort(compare);
    }

    private addStation(station: Station) {
        this.selectedStations.push(station);
    }

    private removeStation(station: Station) {
        this.selectedStations.splice(this.selectedStations.indexOf(station), 1);
    }

    private async submitGeneral() {
        if(this.submitting) return
        this.submitting = true

        let stationIds = [];

        for(const station of this.selectedStations)
          stationIds.push(station.id)

        const response = await fetch(`${config.host}/lines/new`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                name: this.name,
              stationIds: stationIds.join(',')
            })
        })

        const data: Line = await response.json()

        if(data.id)
            await this.$router.push({ name: 'lineView', params: { lineId: `${data.id}` } })
    }

    async mounted() {
        const stationsResponse = await fetch(`${config.host}/stations/list?limit=0`)
        this.stations = (await stationsResponse.json() as PaginationResultType<Station>).results
    }
}
</script>

<style lang="scss">
#line-new {
    .select-box {
        background: white;
        border: solid 1px #e2e2e2;
        border-radius: 4px;
        min-height: 150px;
        width: 100%;
        color: black;
        padding: 5px 0;

        .item {
            display: flex;
            align-items: center;
            padding: 5px 10px;
            font-size: 13px;
            cursor: pointer;
            user-select: none;
            transition: background .2s ease;

            i {
                width: 40px;
                text-align: center;
            }

            &:hover {
                background: #f1f1f1;
            }
        }
    }
}
</style>
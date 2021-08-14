<template>
    <div class="loading-holder">
        <form v-if="hasSearch && data != null" @submit.prevent="$emit('loadMore', data.limit, 0, query)">
            <div class="search-box">
                <input type="text" placeholder="Suche" v-model="query">
                <button type="submit">Suchen</button>
            </div>
        </form>
        <div class="spinner" v-if="data == null"><i class="fas fa-circle-notch fa-spin"></i></div>
        <div v-else-if="data.count === 0">{{ emptyMessage }}</div>
        <template v-else>
            <slot></slot>
            <div class="spinner" v-if="loading"><i class="fas fa-circle-notch fa-spin"></i></div>
            <div class="load-more-holder" v-else-if="data.moreAvailable">
                <div class="load-more-button" @click="$emit('loadMore', data.limit, data.page + 1, data.query)">Mehr laden</div>
            </div>
        </template>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';

@Component
export default class PaginationResult extends Vue {
    @Prop() readonly data!: string;
    @Prop() readonly emptyMessage!: string;
    @Prop() readonly loading!: string;
    @Prop() readonly hasSearch!: string;

    private query: string = ""
}
</script>

<style scoped lang="scss">
.loading-holder {
    .spinner {
        min-height: 150px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: var(--category-color-secondary);
    }

    .search-box {
        height: 40px;
        display: flex;
        border: solid 1px #393E46;
        margin-bottom: 15px;
        border-radius: 4px;
        overflow: hidden;
        color: white;

        input {
            height: 100%;
            flex: 1;
            border: none;
            padding: 0 15px;

            &:focus {
                outline: none;
            }
        }

        button {
            height: 100%;
            display: flex;
            align-items: center;
            background: var(--category-color-gradient);
            color: white;
            padding: 0 15px;
            border: none;
            opacity: .8;
            cursor: pointer;
            transition: opacity .2s ease;

            &:hover {
                opacity: 1;
            }

            &:focus {
                outline: none;
            }
        }
    }

    .load-more-holder {
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 20px 10px;

        .load-more-button {
            background: #393E46;
            border: solid 1px #3b4047;
            font-size: 15px;
            min-height: 40px;
            padding: 0 15px;
            border-radius: 4px;
            color: #ffffff;
            cursor: pointer;
            display: flex;
            align-items: center;
            text-decoration: none;
            transition: background .2s ease;

            i {
                margin-right: 5px;
            }

            &:hover {
                background: #484e56;
            }
        }
    }
}
</style>
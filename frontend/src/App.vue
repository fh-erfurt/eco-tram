<template>
	<div id="app">
		<div class="chin">
			<div class="routes">
				<router-link :to="{ name: level.name, params: level.params || {} }" class="item" v-for="(level, index) in levels" :key="index">
					<div class="image" :class="{ rounded: level.image.rounded }" v-if="level.image" :style="{ backgroundImage: `url(${ level.image.url })` }"></div>
					<i :class="level.icon" v-if="level.icon" :style="level.iconColor ? { color: level.iconColor } : null"></i>
					{{ level.displayName }}
				</router-link>
			</div>
		</div>
		<div class="inner">
			<div class="scrollable-content">
                <div id="content">
				    <router-view></router-view>
                </div>
			</div>
		</div>
	</div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';
import { routeMeta } from '@/router/index'
import { Level } from '@/types'

@Component
export default class App extends Vue {
	private levels: Level[] = []

	mounted() {
		const levels: Level[] = [routeMeta.root]

		for(const route of this.$route.matched) {
			if(route.meta.customParents)
				for(const parent of route.meta.customParents)
					levels.push(parent)
			
			if(route.meta.displayName)
				levels.push({
					path: route.path,
					name: route.name!,
					displayName: route.meta.displayName,
					icon: route.meta.icon
				})
		}

		this.levels = [...levels]

		this.$root.$on('change-level', (levels: Level[]) => this.levels = [...levels])

		this.$root.$on('change-level-name', (name: string, displayName: string) => {
			let index = -1
			const levels = this.levels

			for(const i in this.levels)
				if(levels[i].name === name)
					index = parseInt(i)

			if(index !== -1)
				levels[index].displayName = displayName

				this.levels = [...levels]
		})

		this.$root.$on('change-level-data', (name: string, data: Level) => {
			let index = -1
			const levels = this.levels

			for(const i in this.levels)
				if(levels[i].name === name)
					index = parseInt(i)

			if(index !== -1)
				levels[index] = data

				this.levels = [...levels]
		})
	}
}
</script>

<style lang="scss" scoped>
#app {
    display: flex;
    flex-direction: column;
    height: 100vh;

    .chin {
        display: flex;
        align-items: center;
        padding: 15px;

        .routes {
            flex: 1;
            display: flex;
            align-items: center;

            .item {
                padding: 5px 10px;
                border-radius: 4px;
                min-height: 34px;
                color: #a7a7a7;
                font-size: 15px;
                position: relative;
                text-decoration: none;
                display: flex;
                align-items: center;

                .image {
                    height: 24px;
                    width: 24px;
                    border-radius: 3px;
                    background-size: cover;
                    background-position: center;
                    margin-right: 8px;

                    &.rounded {
                        border-radius: 12px;
                    }
                }

                i {
                    color: var(--category-color-secondary);
                    margin-right: 5px;
                    font-size: 18px;
                }

                &:hover {
                    background: rgba(#000, .05);
                }

                & + .item {
                    margin-left: 15px;

                    &::before {
                        content: '/';
                        position: absolute;
                        width: 15px;
                        left: -15px;
                        text-align: center;
                        color: #a7a7a7;
                    }
                }
            }
        }
    }

    .inner {
        flex: 1;
        position: relative;

        .scrollable-content {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            overflow-y: auto;
        }
    }
}
</style>

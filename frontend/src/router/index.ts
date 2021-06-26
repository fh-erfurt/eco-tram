import Vue from "vue"
import VueRouter, { NavigationGuardNext, Route, RouteConfig } from "vue-router"
import { Level } from '@/types'

import Home from "../views/Home.vue"

import Connections from "../views/Connections.vue"
import ConnectionView from "../views/connections/ConnectionView.vue"
import ConnectionEdit from "../views/connections/ConnectionEdit.vue"
import ConnectionNew from "../views/connections/ConnectionNew.vue"

Vue.use(VueRouter)

interface RouteMeta {
	displayName: string
	icon?: string
	name: string,
	customParents?: [RouteMeta]
}

const routeMeta = Object.freeze({
	root: { displayName: "Administration", name: "index" } as RouteMeta,
	connections: { displayName: "Verbindungen", name: "connections" } as RouteMeta,
})

const routes: Array<RouteConfig> = [
	{
		path: "/",
		name: "index",
		component: Home
	},
	{
		path: "/connections",
		name: "connections",
		component: Connections,
		meta: routeMeta.connections
	},
	{
		path: "/connections/new",
		name: "connectionNew",
		component: ConnectionNew,
		meta: {
			displayName: 'Neue Kategorie',
			customParents: [routeMeta.connections]
		},
		props: route => ({ item: route.params.item })
	},
	{
		path: "/connections/:connectionId",
		name: "connectionView",
		component: ConnectionView,
		meta: {
			displayName: '...',
			customParents: [routeMeta.connections]
		},
		props: route => ({ item: route.params.item })
	},
	{
		path: "/connections/:connectionId/edit",
		name: "connectionEdit",
		component: ConnectionEdit,
		meta: {
			displayName: 'Bearbeiten',
			customParents: [
				routeMeta.connections,
				{
					name: 'connectionView',
					displayName: '...'
				},
			]
		},
		props: route => ({ item: route.params.item })
	}
]

const router = new VueRouter({
	mode: "history",
	base: process.env.BASE_URL,
	routes,
})

router.beforeEach(
	(to: Route, _: Route, next: NavigationGuardNext<Vue>) => {
		const levels: Level[] = [routeMeta.root]

		for(const route of to.matched) {
			const meta: RouteMeta = route.meta

			if(meta.customParents)
				for(const parent of meta.customParents)
					levels.push(parent)

			if(meta.displayName)
				levels.push({
					name: route.name!,
					displayName: meta.displayName,
					icon: meta.icon
				})
		}

		router.app.$root.$emit('change-level', levels)

		next()
	}
)

export default router
export { routeMeta }

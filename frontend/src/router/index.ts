import Vue from "vue"
import VueRouter, { NavigationGuardNext, Route, RouteConfig } from "vue-router"
import { Level } from '@/types'

import Home from "../views/Home.vue"

import Connections from "../views/Connections.vue"
import ConnectionView from "../views/connections/ConnectionView.vue"
import ConnectionEdit from "../views/connections/ConnectionEdit.vue"
import ConnectionNew from "../views/connections/ConnectionNew.vue"
import PassengerTrams from "@/views/PassengerTrams.vue";
import Stations from "@/views/Stations.vue";
import Lines from "@/views/Lines.vue";
import StationNew from "@/views/stations/StationNew.vue";
import StationView from "@/views/stations/StationView.vue";
import StationEdit from "@/views/stations/StationEdit.vue";
import LineEdit from "@/views/lines/LineEdit.vue";
import LineView from "@/views/lines/LineView.vue";
import LineNew from "@/views/lines/LineNew.vue";
import PassengerTramEdit from "@/views/passengerTrams/PassengerTramEdit.vue";
import PassengerTramView from "@/views/passengerTrams/PassengerTramView.vue";
import PassengerTramNew from "@/views/passengerTrams/PassengerTramNew.vue";
import Simulation from "@/views/Simulation.vue";

Vue.use(VueRouter)

interface RouteMeta {
	displayName: string
	icon?: string
	name: string,
	customParents?: [RouteMeta]
}

const routeMeta = Object.freeze({
	root: { displayName: "Administration", name: "index" } as RouteMeta,
	connections: { displayName: "Verbindungen", name: "connections", icon: 'fa fa-sitemap' } as RouteMeta,
	lines: { displayName: "Linien", name: "lines", icon: 'fas fa-code-branch' } as RouteMeta,
	passengerTrams: { displayName: "Straßenbahnen", name: "passengerTrams", icon: 'fa fa-subway' } as RouteMeta,
	stations: { displayName: "Stationen", name: "stations", icon: 'fas fa-h-square' } as RouteMeta,
	simulation: { displayName: "Simulation", name: "simulation", icon: 'fas fa-play' } as RouteMeta,
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
			displayName: 'Neue Verbindung',
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
	},
	{
		path: "/lines",
		name: "lines",
		component: Lines,
		meta: routeMeta.lines
	},
	{
		path: "/lines/new",
		name: "lineNew",
		component: LineNew,
		meta: {
			displayName: 'Neue Linie',
			customParents: [routeMeta.lines]
		},
		props: route => ({ item: route.params.item })
	},
	{
		path: "/lines/:lineId",
		name: "lineView",
		component: LineView,
		meta: {
			displayName: '...',
			customParents: [routeMeta.lines]
		},
		props: route => ({ item: route.params.item })
	},
	{
		path: "/lines/:lineId/edit",
		name: "lineEdit",
		component: LineEdit,
		meta: {
			displayName: 'Bearbeiten',
			customParents: [
				routeMeta.lines,
				{
					name: 'lineView',
					displayName: '...'
				},
			]
		},
		props: route => ({ item: route.params.item })
	},
	{
		path: "/passenger-trams",
		name: "passengerTrams",
		component: PassengerTrams,
		meta: routeMeta.passengerTrams
	},
	{
		path: "/passenger-trams/new",
		name: "passengerTramNew",
		component: PassengerTramNew,
		meta: {
			displayName: 'Neue Straßenbahn',
			customParents: [routeMeta.passengerTrams]
		},
		props: route => ({ item: route.params.item })
	},
	{
		path: "/passenger-trams/:passengerTramId",
		name: "passengerTramView",
		component: PassengerTramView,
		meta: {
			displayName: '...',
			customParents: [routeMeta.passengerTrams]
		},
		props: route => ({ item: route.params.item })
	},
	{
		path: "/passenger-trams/:passengerTramId/edit",
		name: "passengerTramEdit",
		component: PassengerTramEdit,
		meta: {
			displayName: 'Bearbeiten',
			customParents: [
				routeMeta.passengerTrams,
				{
					name: 'passengerTramView',
					displayName: '...'
				},
			]
		},
		props: route => ({ item: route.params.item })
	},
	{
		path: "/stations",
		name: "stations",
		component: Stations,
		meta: routeMeta.stations
	},
	{
		path: "/stations/new",
		name: "stationNew",
		component: StationNew,
		meta: {
			displayName: 'Neue Station',
			customParents: [routeMeta.stations]
		},
		props: route => ({ item: route.params.item })
	},
	{
		path: "/stations/:stationId",
		name: "stationView",
		component: StationView,
		meta: {
			displayName: '...',
			customParents: [routeMeta.stations]
		},
		props: route => ({ item: route.params.item })
	},
	{
		path: "/stations/:stationId/edit",
		name: "stationEdit",
		component: StationEdit,
		meta: {
			displayName: 'Bearbeiten',
			customParents: [
				routeMeta.stations,
				{
					name: 'stationView',
					displayName: '...'
				},
			]
		},
		props: route => ({ item: route.params.item })
	},
	{
		path: "/simulation",
		name: "simulation",
		component: Simulation,
		meta: routeMeta.simulation
	},
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

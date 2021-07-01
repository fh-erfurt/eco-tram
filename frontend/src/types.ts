interface Level {
	path?: string
	name: string
	displayName: string
	icon?: string
}

interface EntityBase {
    id: number
}

interface Line extends EntityBase {
    name: string
    route: Line[]|Connection[]
}

interface Tram extends EntityBase {
    weight: number
    maxSpeed: number
}

interface PassengerTram extends Tram {
    maxPassengers: number
}

interface Traversable extends EntityBase {
    length: number
    maxWeight: number
    trafficFactor: number
}

interface Station extends Traversable {
    name: string
    maxPassengers: number
    currentPassengers: number
}

interface Connection extends Traversable {
    sourceStation: Station
    destinationStation: Station
}

interface PaginationResult<T> {
    results: T[]
    moreAvailable: boolean
    page: number
    limit: number
    count: number
    total: number
}

export type { Level, Line, PassengerTram, Station, Connection, Traversable, PaginationResult as PaginationResultType }
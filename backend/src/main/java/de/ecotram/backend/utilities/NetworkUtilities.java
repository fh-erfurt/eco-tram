package de.ecotram.backend.utilities;

import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.entity.network.Network;
import de.ecotram.backend.entity.network.Station;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

public final class NetworkUtilities {
    private static Network network;

    public static Network getTestingNetwork() {
        if (network != null)
            return network;

        // 1
        Station europaPlatz = Station.builder().name("Europaplatz").build();
        Station theuringenPark = Station.builder().name("Thüringenpark").build();
        Station strDerNationen = Station.builder().name("Straße der Nationen").build();
        Station warschauerStr = Station.builder().name("Warschauer Straße").build();
        Station berlinerStr = Station.builder().name("Berliner Straße").build();
        Station vilniuserStr = Station.builder().name("Vilniuser Straße").build();
        Station rieth = Station.builder().name("Rieth").build();
        Station mainzerStr = Station.builder().name("Mainzer Straße").build();
        Station mittelhaeuserStr = Station.builder().name("Mittelhäuser Straße").build();
        Station salinenStr = Station.builder().name("Salinenstraße").build();
        Station ilversgehofnerPlatz = Station.builder().name("Ilversgehofner Platz").build();
        Station wendenStr = Station.builder().name("Wendenstraße").build();
        Station lutherKirche = Station.builder().name("Lutherkirche/SWE").build();
        Station boyneburgUfer = Station.builder().name("Boyneburgufer").build();
        Station augustinerKloster = Station.builder().name("Augustinerkloster").build();
        Station stadtmuseum = Station.builder().name("Stadtmuseum/Kaisersaal").build();
        Station anger = Station.builder().name("Anger").build();
        Station hauptBahnhof = Station.builder().name("Hauptbahnhof").build();
        Station kaffetrichter = Station.builder().name("Kaffetrichter").build();
        Station landtag = Station.builder().name("Landtag/Stadion Nord").build();
        Station humboldStr = Station.builder().name("Humboldstraße").build();
        Station thueringenhalle = Station.builder().name("Thüringenhalle").build();

        europaPlatz.connectToAndFrom(theuringenPark, c -> c);
        theuringenPark.connectToAndFrom(strDerNationen, c -> c);
        strDerNationen.connectToAndFrom(warschauerStr, c -> c);
        warschauerStr.connectToAndFrom(berlinerStr, c -> c);
        berlinerStr.connectToAndFrom(vilniuserStr, c -> c);
        vilniuserStr.connectToAndFrom(rieth, c -> c);
        rieth.connectToAndFrom(mainzerStr, c -> c);
        mainzerStr.connectToAndFrom(mittelhaeuserStr, c -> c);
        mittelhaeuserStr.connectToAndFrom(salinenStr, c -> c);
        salinenStr.connectToAndFrom(ilversgehofnerPlatz, c -> c);
        ilversgehofnerPlatz.connectToAndFrom(wendenStr, c -> c);
        wendenStr.connectToAndFrom(lutherKirche, c -> c);
        lutherKirche.connectToAndFrom(boyneburgUfer, c -> c);
        boyneburgUfer.connectToAndFrom(augustinerKloster, c -> c);
        augustinerKloster.connectToAndFrom(stadtmuseum, c -> c);
        stadtmuseum.connectToAndFrom(anger, c -> c);
        anger.connectToAndFrom(hauptBahnhof, c -> c);
        hauptBahnhof.connectToAndFrom(kaffetrichter, c -> c);
        kaffetrichter.connectToAndFrom(landtag, c -> c);
        landtag.connectToAndFrom(humboldStr, c -> c);
        humboldStr.connectToAndFrom(thueringenhalle, c -> c);

        // 2

        Station messePlatz = Station.builder().name("P+R Platz Messe").build();
        Station messe = Station.builder().name("Messe").build();
        Station mdr = Station.builder().name("MDR/Kinderkanal").build();
        Station egapark = Station.builder().name("Egapark").build();
        Station gothaerPlatz = Station.builder().name("Gothaer Platz").build();
        Station finanzZentrum = Station.builder().name("Finanzzentrum").build();
        Station theater = Station.builder().name("Theater").build();
        Station domplatzSued = Station.builder().name("Domplatz Süd").build();
        Station fishmarkt = Station.builder().name("Fishmarkt/Rathaus").build();
        Station robertKochStr = Station.builder().name("Robert-Koch-Straße").build();
        Station tschaikowskiStr = Station.builder().name("Tschaikowskistraße").build();
        Station stadionOst = Station.builder().name("Stadion Ost").build();
        Station amSchwemmbach = Station.builder().name("Am Schwemmbach").build();
        Station sozialversicherungs = Station.builder().name("Sozialversicherungs").build();
        Station bluecherStr = Station.builder().name("Blücherstraße").build();
        Station abzweigWiesenhuegel = Station.builder().name("Abzweig Wiesenhügel").build();
        Station faerberweidWeg = Station.builder().name("Färberweidweg").build();
        Station wiesenhuegel = Station.builder().name("Wiesenhügel").build();

        messePlatz.connectToAndFrom(messe, c -> c);
        messe.connectToAndFrom(mdr, c -> c);
        mdr.connectToAndFrom(egapark, c -> c);
        egapark.connectToAndFrom(gothaerPlatz, c -> c);
        gothaerPlatz.connectToAndFrom(finanzZentrum, c -> c);
        finanzZentrum.connectToAndFrom(theater, c -> c);
        theater.connectToAndFrom(domplatzSued, c -> c);
        domplatzSued.connectToAndFrom(fishmarkt, c -> c);
        fishmarkt.connectToAndFrom(anger, c -> c);

        hauptBahnhof.connectToAndFrom(robertKochStr, c -> c);
        robertKochStr.connectToAndFrom(tschaikowskiStr, c -> c);
        tschaikowskiStr.connectToAndFrom(stadionOst, c -> c);
        stadionOst.connectToAndFrom(amSchwemmbach, c -> c);
        amSchwemmbach.connectToAndFrom(sozialversicherungs, c -> c);
        sozialversicherungs.connectToAndFrom(bluecherStr, c -> c);
        bluecherStr.connectToAndFrom(abzweigWiesenhuegel, c -> c);
        abzweigWiesenhuegel.connectToAndFrom(faerberweidWeg, c -> c);
        faerberweidWeg.connectToAndFrom(wiesenhuegel, c -> c);

        // 3
        Station riethStr = Station.builder().name("Riethstraße").build();
        Station klinikum = Station.builder().name("Klinikum").build();
        Station universitaet = Station.builder().name("Universitaet").build();
        Station baumStr = Station.builder().name("BaumStr").build();
        Station bergStr = Station.builder().name("BergStr").build();
        Station weberGasse = Station.builder().name("Webergasse").build();
        Station domplatzNord = Station.builder().name("Domplatz Nord").build();
        Station melchendorf = Station.builder().name("Melchendorf").build();
        Station melchendorferMarkt = Station.builder().name("Melchendorfer Markt").build();
        Station drosselberg = Station.builder().name("Drosselberg").build();
        Station katholischesKrankenhaus = Station.builder().name("Katholisches Krankenhaus").build();
        Station xFab = Station.builder().name("Windischholzhausen/X-FAB").build();
        Station urbischerKreuz = Station.builder().name("Urbischer Kreuz").build();

        warschauerStr.connectToAndFrom(riethStr, c -> c);
        riethStr.connectToAndFrom(klinikum, c -> c);
        klinikum.connectToAndFrom(universitaet, c -> c);
        universitaet.connectToAndFrom(baumStr, c -> c);
        baumStr.connectToAndFrom(bergStr, c -> c);
        bergStr.connectToAndFrom(weberGasse, c -> c);
        weberGasse.connectToAndFrom(domplatzNord, c -> c);

        abzweigWiesenhuegel.connectToAndFrom(melchendorf, c -> c);
        melchendorf.connectToAndFrom(melchendorferMarkt, c -> c);
        melchendorferMarkt.connectToAndFrom(drosselberg, c -> c);
        drosselberg.connectToAndFrom(katholischesKrankenhaus, c -> c);
        katholischesKrankenhaus.connectToAndFrom(xFab, c -> c);
        xFab.connectToAndFrom(urbischerKreuz, c -> c);

        // 4
        Station bindersleben = Station.builder().name("Bindersleben").build();
        Station bueroPark = Station.builder().name("Büropark").build();
        Station orionStr = Station.builder().name("Orionstraße").build();
        Station volkenroderWeg = Station.builder().name("VolkenroderWeg").build();
        Station amKreuzchen = Station.builder().name("Am Kreuzchen").build();
        Station hauptfriedhof = Station.builder().name("Hauptfriedhof").build();
        Station walterGropiusStr = Station.builder().name("Walter-Gropius-Straße").build();
        Station nibelungenWeg = Station.builder().name("Nibelungenweg").build();
        Station gamstaedterStr = Station.builder().name("Gamstädterstraße").build();
        Station bundesbreitsgericht = Station.builder().name("Bundesbreitsgericht").build();
        Station justitzZentrum = Station.builder().name("Justitz Zentrum").build();
        Station gorkiStr = Station.builder().name("Gorkistraße").build();
        Station bruehlerGarten = Station.builder().name("Brühler Garten").build();
        Station langeBruecke = Station.builder().name("Lange Bräcke").build();
        Station karlMarxPlatz = Station.builder().name("Karl-Marx-Platz").build();
        Station angerBrunnen = Station.builder().name("Angerbrunnen").build();
        Station hirschGarten = Station.builder().name("Hirschgarten").build();
        Station kraempferTor = Station.builder().name("Krämpfertor").build();
        Station leipzigerPlatz = Station.builder().name("Leipziger Platz").build();
        Station hansePlatz = Station.builder().name("Hanseplatz").build();
        Station greifswalderStr = Station.builder().name("Greifswalder Straße").build();
        Station kraempferGaerten = Station.builder().name("Krämpfer Gärten").build();
        Station bautznerWeg = Station.builder().name("Bautzner Weg").build();
        Station marcelBreuerRing = Station.builder().name("Marcel-Breuer-Ring").build();
        Station wagenfelderStr = Station.builder().name("Wagenfelderstraße").build();
        Station ringelBerg = Station.builder().name("Ringelberg").build();

        bindersleben.connectToAndFrom(bueroPark, c -> c);
        bueroPark.connectToAndFrom(orionStr, c -> c);
        orionStr.connectToAndFrom(volkenroderWeg, c -> c);
        volkenroderWeg.connectToAndFrom(amKreuzchen, c -> c);
        amKreuzchen.connectToAndFrom(hauptfriedhof, c -> c);
        hauptfriedhof.connectTo(nibelungenWeg, c -> c);
        walterGropiusStr.connectTo(hauptfriedhof, c -> c);
        nibelungenWeg.connectTo(walterGropiusStr, c -> c);
        nibelungenWeg.connectToAndFrom(gamstaedterStr, c -> c);
        gamstaedterStr.connectToAndFrom(bundesbreitsgericht, c -> c);
        bundesbreitsgericht.connectToAndFrom(justitzZentrum, c -> c);
        justitzZentrum.connectToAndFrom(gothaerPlatz, c -> c);

        finanzZentrum.connectToAndFrom(gorkiStr, c -> c);
        gorkiStr.connectToAndFrom(bruehlerGarten, c -> c);
        bruehlerGarten.connectToAndFrom(langeBruecke, c -> c);
        bruehlerGarten.connectTo(karlMarxPlatz, c -> c);
        karlMarxPlatz.connectTo(hirschGarten, c -> c);
        hirschGarten.connectTo(anger, c -> c);
        anger.connectTo(angerBrunnen, c -> c);
        angerBrunnen.connectTo(langeBruecke, c -> c);
        anger.connectToAndFrom(kraempferTor, c -> c);
        kraempferTor.connectToAndFrom(leipzigerPlatz, c -> c);
        leipzigerPlatz.connectToAndFrom(hansePlatz, c -> c);
        hansePlatz.connectToAndFrom(greifswalderStr, c -> c);
        greifswalderStr.connectToAndFrom(kraempferGaerten, c -> c);
        kraempferGaerten.connectToAndFrom(bautznerWeg, c -> c);
        bautznerWeg.connectToAndFrom(marcelBreuerRing, c -> c);
        marcelBreuerRing.connectToAndFrom(wagenfelderStr, c -> c);
        wagenfelderStr.connectToAndFrom(ringelBerg, c -> c);

        // 5
        Station zooPark = Station.builder().name("Zoopark").build();
        Station roterBerg = Station.builder().name("Roter Berg").build();
        Station augustFroehlichStr = Station.builder().name("August-Fröhlich-Straße").build();
        Station bunsenStr = Station.builder().name("Bunsenstraße").build();
        Station anDerLAche = Station.builder().name("An Der Lache").build();
        Station grubenStr = Station.builder().name("Grubenstraße").build();

        zooPark.connectToAndFrom(roterBerg, c -> c);
        roterBerg.connectToAndFrom(augustFroehlichStr, c -> c);
        augustFroehlichStr.connectToAndFrom(bunsenStr, c -> c);
        bunsenStr.connectToAndFrom(anDerLAche, c -> c);
        anDerLAche.connectToAndFrom(grubenStr, c -> c);
        grubenStr.connectToAndFrom(salinenStr, c -> c);

        // 6
        Station steigerStr = Station.builder().name("Steigerstraße").build();
        Station hochheimerStr = Station.builder().name("Hochheimerstraße").build();
        Station milchInselStr = Station.builder().name("Milchinselstraße").build();
        Station pushkinStr = Station.builder().name("Pushkinstraße").build();

        steigerStr.connectTo(milchInselStr, c -> c);
        hochheimerStr.connectTo(steigerStr, c -> c);
        milchInselStr.connectTo(pushkinStr, c -> c);
        pushkinStr.connectTo(hochheimerStr, c -> c);
        pushkinStr.connectToAndFrom(kaffetrichter, c -> c);

        riethStr.connectToAndFrom(berlinerStr, c -> c);


        HashSet<Station> stations = new HashSet<>() {{
            add(europaPlatz);
            add(theuringenPark);
            add(strDerNationen);
            add(warschauerStr);
            add(berlinerStr);
            add(vilniuserStr);
            add(rieth);
            add(mainzerStr);
            add(mittelhaeuserStr);
            add(salinenStr);
            add(ilversgehofnerPlatz);
            add(wendenStr);
            add(lutherKirche);
            add(boyneburgUfer);
            add(augustinerKloster);
            add(stadtmuseum);
            add(anger);
            add(hauptBahnhof);
            add(kaffetrichter);
            add(landtag);
            add(humboldStr);
            add(thueringenhalle);
            add(messePlatz);
            add(messe);
            add(mdr);
            add(egapark);
            add(gothaerPlatz);
            add(finanzZentrum);
            add(theater);
            add(domplatzSued);
            add(fishmarkt);
            add(robertKochStr);
            add(tschaikowskiStr);
            add(stadionOst);
            add(amSchwemmbach);
            add(sozialversicherungs);
            add(bluecherStr);
            add(abzweigWiesenhuegel);
            add(faerberweidWeg);
            add(wiesenhuegel);
            add(riethStr);
            add(klinikum);
            add(universitaet);
            add(baumStr);
            add(bergStr);
            add(weberGasse);
            add(domplatzNord);
            add(melchendorf);
            add(melchendorferMarkt);
            add(drosselberg);
            add(katholischesKrankenhaus);
            add(xFab);
            add(urbischerKreuz);
            add(bindersleben);
            add(bueroPark);
            add(orionStr);
            add(volkenroderWeg);
            add(amKreuzchen);
            add(hauptfriedhof);
            add(walterGropiusStr);
            add(nibelungenWeg);
            add(gamstaedterStr);
            add(bundesbreitsgericht);
            add(justitzZentrum);
            add(gorkiStr);
            add(bruehlerGarten);
            add(langeBruecke);
            add(karlMarxPlatz);
            add(angerBrunnen);
            add(hirschGarten);
            add(kraempferTor);
            add(leipzigerPlatz);
            add(hansePlatz);
            add(greifswalderStr);
            add(kraempferGaerten);
            add(bautznerWeg);
            add(marcelBreuerRing);
            add(wagenfelderStr);
            add(ringelBerg);
            add(zooPark);
            add(roterBerg);
            add(augustFroehlichStr);
            add(bunsenStr);
            add(anDerLAche);
            add(grubenStr);
            add(steigerStr);
            add(hochheimerStr);
            add(milchInselStr);
            add(pushkinStr);
        }};

        network = Network.fromStations(stations);
        network.initialize();

        return network;
    }

    // TODO(erik): optimize
    public static DistanceTree dijkstra(Station start, Map<Station, Set<Station>> adjacencyMap) {
        if (!adjacencyMap.containsKey(start))
            throw new InvalidParameterException("The start station does not exist in this network.");

        // initialize
        Station currentMinimum = start;
        Set<Station> traversed = new HashSet<>(adjacencyMap.size());
        Set<Station> nodeQueue = new HashSet<>() {{
            add(start);
        }};
        Map<Station, NetworkUtilities.DijkstraTuple> distanceMap = new HashMap<>(adjacencyMap.keySet()
                .stream()
                .collect(Collectors.toMap(
                        k -> k,
                        v -> new NetworkUtilities.DijkstraTuple(Integer.MAX_VALUE, 0, null))
                )
        );

        distanceMap.get(currentMinimum).distance = 0;

        // avoid constant re-allocation of unchanging collection
        Set<Map.Entry<Station, NetworkUtilities.DijkstraTuple>> distanceEntrySet = distanceMap.entrySet();

        while (!nodeQueue.isEmpty()) {
            // compute distances and previous
            for (Map.Entry<Station, NetworkUtilities.DijkstraTuple> entry : distanceEntrySet) {
                Station key = entry.getKey();
                NetworkUtilities.DijkstraTuple value = entry.getValue();

                // get distance min -> entry
                Optional<Connection> minimumToCurrent = currentMinimum.getConnectionTo(key);
                if (minimumToCurrent.isPresent()) {
                    // distance from start to current
                    int distance = currentMinimum == start
                            ? minimumToCurrent.get().getLength()
                            : distanceMap.get(currentMinimum).distance + minimumToCurrent.get().getLength();

                    // hops from start to current
                    int hops = key == start ? 0
                            : currentMinimum == start ? 1
                            : distanceMap.get(currentMinimum).hops + 1;

                    // if smaller assign as new previous etc
                    if (value.distance > distance) {
                        value.distance = distance;
                        value.hops = hops;
                        value.previous = currentMinimum;

                        // add to queue
                        if (!traversed.contains(key))
                            nodeQueue.add(key);
                    }
                }
            }

            // find new minimum
            currentMinimum = nodeQueue
                    .stream()
                    .min(Comparator.comparingInt(entry -> distanceMap.get(entry).distance))
                    .get();

            // mark traversed
            traversed.add(currentMinimum);
            nodeQueue.remove(currentMinimum);
        }

        return new DistanceTree(start, distanceMap);
    }

    @Data
    @AllArgsConstructor
    public static final class DijkstraTuple {
        private int distance;
        private int hops;
        private Station previous;
    }

    public static final record DistanceTree(Station root, Map<Station, DijkstraTuple> paths) {
        public List<Station> getPathTo(Station destination) {
            LinkedList<Station> list = new LinkedList<>();

            Station previous = destination;
            while (previous != null) {
                list.addFirst(previous);
                previous = this.paths.get(previous).previous;
            }

            return list;
        }

        public int getDistanceTo(Station destination) {
            return this.paths.get(destination).getDistance();
        }

        public int getHopsTo(Station destination) {
            return this.paths.get(destination).getHops();
        }

        public Station getPreviousOf(Station destination) {
            return this.paths.get(destination).getPrevious();
        }
    }
}

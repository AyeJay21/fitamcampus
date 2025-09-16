<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"
          integrity="sha256-p4NxAoJBhIIN+hmNHrzRCf9tD/miZyoHS5obTRR9BMY=" crossorigin=""/>
    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"
            integrity="sha256-20nQCchB9co0qIjJZRGuk2/Z9VM+kNiyxNV1lvTlZBo=" crossorigin=""></script>
    <style>
        #map {
            width: 300px;
            height: 300px;
        }
    </style>
</head>
<body>
<h1>Hello Map</h1>
<button onclick="openstreetMapDisplay()">Map</button>
</body>
<div id="map"></div>

<script>
    async function openstreetMapDisplay() {
        var result = await fetch(
            "https://overpass-api.de/api/interpreter",
            {
                method: "POST",
                // The body contains the query
                // to understand the query language see "The Programmatic Query Language" on
                // https://wiki.openstreetmap.org/wiki/Overpass_API#The_Programmatic_Query_Language_(OverpassQL)
                body: "data=" + encodeURIComponent(`
            [bbox:30.618338,-96.323712,30.591028,-96.330826]
            [out:json]
            [timeout:90]
            ;
            (
                way
                    (
                         30.626917110746,
                         -96.348809105664,
                         30.634468750236,
                         -96.339893442898
                     );
            );
            out geom;
        `)
            },
        ).then(
            (data) => data.json()
        )

        console.log(JSON.stringify(result, null, 2))
    }
</script>
<script>
    var map = L.map('map').setView([0,0], 1);

    L.tileLayer('https://api.maptiler.com/maps/streets-v2/{z}/{x}/{y}.png?key=99g6vNXN3EahQ3ff4f7k', {
        attribution:'<a href="https://www.maptiler.com/copyright/" target="_blank">&copy; MapTiler</a> <a href="https://www.openstreetmap.org/copyright" target="_blank">&copy; OpenStreetMap contributors</a>'
    }).addTo(map);

    L.marker([50.5, 30.5]).addTo(map);

</script>
</html>

var express = require('express');
var app = express();
const axios = require('axios');
console.log(axios.isCancel('something'));
var geohash = require('ngeohash');
var SpotifyWebApi = require('spotify-web-api-node');

const cors = require('cors')
const path = require('path')

app.use(cors())
app.use(express.static(path.resolve(__dirname,'./build')));


var spotifyApi = new SpotifyWebApi({
    clientId: '<Your clientID>',
    clientSecret: '<Your Key>',
  });



  spotifyApi.clientCredentialsGrant().then(
    function(data) {
      console.log('The access token expires in ' + data.body['expires_in']);
      console.log('The access token is ' + data.body['access_token']);
  
      // Save the access token so that it's used in future calls
      spotifyApi.setAccessToken(data.body['access_token']);
    },
    function(err) {
      console.log('Something went wrong when retrieving an access token', err);
    }
  );


app.get('/suggest', async (req,res) => {
    try {
        let keyword = req.query.keyword
        const response = await axios.get('https://app.ticketmaster.com/discovery/v2/suggest?apikey=tl9YgQAGCVrRhB9RGEJCjae9oIMtImav', {
            params: {
                keyword: keyword
            }
        }
        );
        console.log(response.data);
        res.status(200).json(response.data);
    } catch (err) {
        res.status(500).json({ message: err });

    }

});


app.get("/events", async (req, res) => {

    try {
        let keyword = req.query.keyword
        let segmentId = req.query.category
        let radius = req.query.radius
        let lat = req.query.latitude
        let lon = req.query.longitude
        let geopoint = geohash.encode(lat, lon)
        
        console.log(keyword,segmentId,radius,geopoint)
        const response = await axios.get('https://app.ticketmaster.com/discovery/v2/events.json?apikey=tl9YgQAGCVrRhB9RGEJCjae9oIMtImav', {
            params: {
                keyword: keyword, segmentId: segmentId, radius: radius, geoPoint: geopoint, unit: "miles"
            }
        }
        );
        console.log( "recieved data:",response.data);
        res.status(200).json(response.data);
    } catch (err) {
        res.status(500).json({ message: err });
    }
});

app.get('/event_details', async (req, res) => {


    try {
        let event_id = req.query.event_id;
        console.log("entered details: event id: ", event_id)
        const response = await axios.get('https://app.ticketmaster.com/discovery/v2/events/'+event_id+'?apikey=tl9YgQAGCVrRhB9RGEJCjae9oIMtImav');
        console.log("backend details results",response.data);
        res.status(200).json(response.data);
    } catch (err) {
        res.status(500).json({ message: err });
    }

});





app.get('/venue_details', async (req,res) => {
    try{
        let venue_name = req.query.keyword;
        console.log("venue:", req.query)
        var url = 'https://app.ticketmaster.com/discovery/v2/venues.json?keyword='+venue_name+'&apikey=tl9YgQAGCVrRhB9RGEJCjae9oIMtImav';
        console.log("url venue:", url)
        // let venue_name = 'Walt Disney Concert Hall'
        console.log("entered venue:" , venue_name);
        const response = await axios.get(url);
        console.log( "recieved data for venue:",response.data);
        res.status(200).json(response.data);
    } catch (err) {
        res.status(500).json({ message: err });
    }
});

app.get('/spotify', async (req,res) => {
    try{
        let artist_name = req.query.name;
        var result;
        try{
            result = await spotifyApi.searchArtists(artist_name);
        } catch (e) {
            if(e.body.error.status===401) {
                await spotifyApi.clientCredentialsGrant().then(
                    function(data) {
                      console.log('The access token expires in ' + data.body['expires_in']);
                      console.log('The access token is ' + data.body['access_token']);
                  
                      // Save the access token so that it's used in future calls
                      spotifyApi.setAccessToken(data.body['access_token']);
                    },
                    function(err) {
                      console.log('Something went wrong when retrieving an access token', err);
                    }
                );
                result = await spotifyApi.searchArtists(artist_name);
            }
        }
        var artists = result.body.artists.items;
        var artist = {"error": "No artist"}
        for(var i=0;i<artists.length;i++) {
            console.log(artists[i].name)
            if(artists[i].name.toLowerCase() === artist_name.toLowerCase()) {
                artist = artists[i]
                break
            }
        }
        res.status(200).json(artist)
    } catch (err) {
        res.status(500).json({ message: err });
    }
});
  

app.get('/spotify_albums', async (req,res) => {
    try{
        let artist_id = req.query.id;
        var result;
        try{
            result = await spotifyApi.getArtistAlbums(artist_id, {limit: 3});
        } catch (e) {
            if(e.body.error.status===401) {
                await spotifyApi.clientCredentialsGrant().then(
                    function(data) {
                      console.log('The access token expires in ' + data.body['expires_in']);
                      console.log('The access token is ' + data.body['access_token']);
                  
                      // Save the access token so that it's used in future calls
                      spotifyApi.setAccessToken(data.body['access_token']);
                    },
                    function(err) {
                      console.log('Something went wrong when retrieving an access token', err);
                    }
                );
                result = await spotifyApi.getArtistAlbums(artist_id, {limit: 3});
            }
        }
        var albums = result.body.items;
        res.status(200).json(albums)
    } catch (err) {
        res.status(500).json({ message: err });
    }
});
  

 



// app.get('*',(req,res) => {
//     res.sendFile(path.resolve(__dirname,'./build','index.html'));
// })

var server = app.listen(process.env.PORT || 4000, function () {
    // var host = server.address().address
    var port = server.address().port

    console.log("Example app listening at http://localhost:%s", port)
})
// DOMContentLoaded 이벤트가 발생한 후에 Kakao Maps 로드
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOMContentLoaded: Kakao Maps will now be initialized');

    var mapContainer = document.getElementById('map');
    var mapOption = {
        center: new kakao.maps.LatLng(37.5665, 126.9780),
        level: 5
    };

    var map = new kakao.maps.Map(mapContainer, mapOption);
    console.log('Kakao Map initialized');

    var places = new kakao.maps.services.Places();
    var polylines = [];
    var markers = [];

    document.getElementById('directionsForm').addEventListener('submit', function(event) {
        event.preventDefault();
        console.log('Kakao directions form submitted');
        const origin = document.getElementById('origin').value;
        const destination = document.getElementById('destination').value;
        console.log(`Origin: ${origin}, Destination: ${destination}`);

        // Rest of the logic here...
    });
});

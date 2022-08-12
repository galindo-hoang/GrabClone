import MapBoxGeocoder from '@mapbox/mapbox-gl-geocoder';
import { useControl } from 'react-map-gl';

const accessToken = "pk.eyJ1IjoicGhhbXRpZW5xdWFuIiwiYSI6ImNsNXFvb2h3ejB3NGMza28zYWx2enoyem4ifQ.v-O4lWtgCXbhJbPt5nPFIQ";


export const Geocoder = () => {
  const ctrl = new MapBoxGeocoder({
    accessToken: accessToken,
    marker: true,
    collapsed: true,
  });
  useControl(() => ctrl);
  ctrl.on('result', (e) => {
    const coords = e.result.geometry.coordinates;
  });
  return null;
};

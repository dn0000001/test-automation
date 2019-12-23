package com.automation.common.converters;

import com.automation.common.data.Weather;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import datainstiller.data.DataValueConverter;

import java.lang.reflect.Field;

/**
 * This the converter for the information returned from the weather service response
 */
public class WeatherConverter implements DataValueConverter {
    private static final String success = "Success";
    private static final String responseText = "ResponseText";
    private static final String state = "State";
    private static final String city = "City";
    private static final String weatherStationCity = "WeatherStationCity";
    private static final String weatherID = "WeatherID";
    private static final String description = "Description";
    private static final String temperature = "Temperature";
    private static final String relativeHumidity = "RelativeHumidity";
    private static final String wind = "Wind";
    private static final String pressure = "Pressure";
    private static final String visibility = "Visibility";
    private static final String windChill = "WindChill";
    private static final String remarks = "Remarks";

    /**
     * Convert an object to textual data. This method should write the XML structure needed for the request to
     * be send this object as a parameter. If object is not being sent as a parameter, then XML structure
     * written may not matter.
     *
     * @param source  - The object to be marshalled
     * @param writer  - A stream to write to
     * @param context - A context that allows nested objects to be processed by XStream.
     */
    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        Weather weather = (Weather) source;

        writer.startNode(success);
        writer.addAttribute(responseText, responseText);
        writer.setValue(String.valueOf(weather.isSuccess()));
        writer.endNode();

        writer.startNode(state);
        writer.setValue(weather.getState());
        writer.endNode();

        writer.startNode(city);
        writer.setValue(weather.getCity());
        writer.endNode();

        writer.startNode(weatherStationCity);
        writer.setValue(weather.getWeatherStationCity());
        writer.endNode();

        writer.startNode(weatherID);
        writer.setValue(String.valueOf(weather.getWeatherID()));
        writer.endNode();

        writer.startNode(description);
        writer.setValue(weather.getDescription());
        writer.endNode();

        writer.startNode(temperature);
        writer.setValue(String.valueOf(weather.getTemperature()));
        writer.endNode();

        writer.startNode(relativeHumidity);
        writer.setValue(String.valueOf(weather.getRelativeHumidity()));
        writer.endNode();

        writer.startNode(wind);
        writer.setValue(weather.getWind());
        writer.endNode();

        writer.startNode(pressure);
        writer.setValue(weather.getPressure());
        writer.endNode();

        writer.startNode(visibility);
        writer.setValue(weather.getVisibility());
        writer.endNode();

        writer.startNode(windChill);
        writer.setValue(weather.getWindChill());
        writer.endNode();

        writer.startNode(remarks);
        writer.setValue(weather.getRemarks());
        writer.endNode();
    }

    /**
     * Convert textual data back into an object. This method needs to model the XML structure that you want to
     * convert to an object.
     *
     * @param reader  - The stream to read the text from
     * @param context - A context that allows nested objects to be processed by XStream
     */
    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Weather weather = new Weather();

        while (reader.hasMoreChildren()) {
            reader.moveDown();

            //
            // For this example, I am handling both soap requests and get requests as such the following is
            // needed for soap requests. Normally, you would not do this.
            //
            if (reader.getNodeName().equalsIgnoreCase("soap:Body")) {
                reader.moveDown();
                reader.moveDown();
                reader.moveDown();
            }

            switch (reader.getNodeName()) {
                case success:
                    weather.setSuccess(Boolean.valueOf(reader.getValue()));
                    break;

                case responseText:
                    weather.setResponseText(reader.getValue());
                    break;

                case state:
                    weather.setState(reader.getValue());
                    break;

                case city:
                    weather.setCity(reader.getValue());
                    break;

                case weatherStationCity:
                    weather.setWeatherStationCity(reader.getValue());
                    break;

                case weatherID:
                    weather.setWeatherID(Integer.valueOf(reader.getValue()));
                    break;

                case description:
                    weather.setDescription(reader.getValue());
                    break;

                case temperature:
                    weather.setTemperature(Integer.valueOf(reader.getValue()));
                    break;

                case relativeHumidity:
                    weather.setRelativeHumidity(Integer.valueOf(reader.getValue()));
                    break;

                case wind:
                    weather.setWind(reader.getValue());
                    break;

                case pressure:
                    weather.setPressure(reader.getValue());
                    break;

                case visibility:
                    weather.setVisibility(reader.getValue());
                    break;

                case windChill:
                    weather.setWindChill(reader.getValue());
                    break;

                case remarks:
                    weather.setRemarks(reader.getValue());
                    break;

                default:
                    break;
            }

            reader.moveUp();
        }

        return weather;
    }

    /**
     * Determines whether the converter can marshall a particular type.
     *
     * @param type - The Class representing the object type to be converted
     */
    @Override
    public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
        return type.equals(Weather.class);
    }

    /**
     * Converts the string to the desired object
     *
     * @param str   - String to convert into object
     * @param cls   - The Class representing the object type to be converted
     * @param field - Is not used in this specific implementation
     * @return Object that converter is for
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T fromString(String str, Class<T> cls, Field field) {
        Weather weather = new Weather();

        weather.setSuccess(false);
        weather.setResponseText(responseText);
        weather.setState(state);
        weather.setCity(city);
        weather.setWeatherStationCity(weatherStationCity);
        weather.setWeatherID(-1);
        weather.setDescription(description);
        weather.setTemperature(0);
        weather.setRelativeHumidity(0);
        weather.setWind(wind);
        weather.setPressure(pressure);
        weather.setVisibility(visibility);
        weather.setWindChill(windChill);
        weather.setRemarks(remarks);

        return (T) weather;
    }

}

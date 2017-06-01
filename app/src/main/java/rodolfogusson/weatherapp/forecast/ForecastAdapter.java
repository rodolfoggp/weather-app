package rodolfogusson.weatherapp.forecast;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rodolfogusson.weatherapp.R;
import rodolfogusson.weatherapp.model.Weather;
import rodolfogusson.weatherapp.utilities.LayoutUtils;

/**
 * Created by rodolfo on 5/31/17.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastListViewHolder> {
    private List<Weather> weathers;
    private Context mContext;
    private String city, country;

    public ForecastAdapter(Context context, List<Weather> list, String city, String country) {
        buildForecastList(list);
        this.mContext = context;
        this.city = city;
        this.country = country;
    }

    private void buildForecastList(List<Weather> list){
        List<Weather> forecastWeathers = new ArrayList<>();
        /*We won't include the first weather, as it is the weather right now.
          That does not belong in the forecast list.
          So, we begin adding elements from index = 1
          We will include six elements, to show weather information about one
          week, combined with the Weather Now card on main screen.*/
        for(int index = 1; index < 7; index++){
            forecastWeathers.add(list.get(index));
        }
        weathers = forecastWeathers;
    }

    @Override
    public ForecastListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.forecast_list_item, null);
        return new ForecastListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastListViewHolder viewHolder, int i) {
        Weather weather = weathers.get(i);
        viewHolder.dayOfWeek.setText(weather.getDate().dayOfWeek().getAsText(Locale.US));
        viewHolder.icon.setImageBitmap(weather.getIcon());
        viewHolder.condition.setText(weather.getCurrentCondition().getCondition());
        LayoutUtils utils = LayoutUtils.getInstance().init(mContext);
        viewHolder.high.setText(utils.getConvertedTemperatureWithUnit(
                weather.getTemperature().getMaxTemp()));
        viewHolder.low.setText(utils.getConvertedTemperatureWithUnit(
                weather.getTemperature().getMinTemp()));
    }

    @Override
    public int getItemCount() {
        return (null != weathers ? weathers.size() : 0);
    }

    public void clear(){
        weathers.clear();
        notifyDataSetChanged();
    }

    class ForecastListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected ImageView icon;
        protected TextView dayOfWeek, condition, high, low;

        public ForecastListViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            dayOfWeek = (TextView) view.findViewById(R.id.day_of_week);
            icon = (ImageView) view.findViewById(R.id.icon);
            condition = (TextView) view.findViewById(R.id.condition);
            high = (TextView) view.findViewById(R.id.high);
            low = (TextView) view.findViewById(R.id.low);
        }

        @Override
        public void onClick(View v) {
            int pos = getLayoutPosition();
            Intent intent = new Intent(mContext, DetailsActivity.class);
            intent.putExtra("weather",weathers.get(pos));
            intent.putExtra("city",city);
            intent.putExtra("country",country);
            mContext.startActivity(intent);
        }
    }
}
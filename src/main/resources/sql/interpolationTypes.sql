insert
  into time_series_interpolation_types (json_data_id,
                                        start_time,
                                        end_time,
                                        interpolation_type
                                       )
select json_data_id,
       adjust_timestamp(jsonb_extract_path_text(interpolation_types, 'StartTime')) start_time,
       adjust_timestamp(jsonb_extract_path_text(interpolation_types, 'EndTime')) end_time,
       jsonb_extract_path_text(interpolation_types, 'Type') interpolation_type
  from (select json_data_id,
               jsonb_array_elements(jsonb_extract_path(json_content, 'InterpolationTypes')) interpolation_types
          from json_data
         where json_data_id = ?) a
on conflict on constraint time_series_interpolation_types_ak do nothing

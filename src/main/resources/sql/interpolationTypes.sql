insert
  into time_series_interpolation_types (time_series_unique_id,
                                        start_time,
                                        end_time,
                                        interpolation_type,
                                        response_time,
                                        response_version
                                       )
select time_series_unique_id,
       jsonb_extract_path_text(interpolation_types, 'StartTime')::timestamp start_time,
       jsonb_extract_path_text(interpolation_types, 'EndTime')::timestamp end_time,
       jsonb_extract_path_text(interpolation_types, 'Type') interpolation_type,
       response_time,
       response_version
  from (select jsonb_extract_path_text(json_content, 'UniqueId') time_series_unique_id,
               jsonb_array_elements(jsonb_extract_path(json_content, 'InterpolationTypes')) interpolation_types
               jsonb_extract_path_text(json_content, 'ResponseTime')::timestamp response_time,
               jsonb_extract_path_text(json_content, 'ResponseVersion')::numeric response_version
          from json_data
         where json_data_id = ?) a
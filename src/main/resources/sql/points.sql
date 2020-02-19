insert
  into time_series_points(json_data_id,
                          time_step,
                          numeric_value,
                          display_value
                         )
select json_data_id,
       adjust_timestamp(jsonb_extract_path_text(points, 'Timestamp')) time_step,
       jsonb_extract_path_text(points, 'Value', 'Numeric')::numeric numeric_value,
       jsonb_extract_path_text(points, 'Value', 'Display') display_value
  from (select json_data_id,
               jsonb_array_elements(jsonb_extract_path(json_content, 'Points')) points
          from json_data
         where json_data_id = ?) a

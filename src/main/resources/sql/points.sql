insert
  into time_series_points (time_series_unique_id,
                           time_step,
                           numeric_value,
                           display_value,
                           notes,
                           summary,
                           num_points,
                           time_range_start,
                           time_range_end,
                           response_time,
                           response_version,
                           location_identifier
                          )
select time_series_unique_id,
       jsonb_extract_path_text(points, 'Timestamp')::timestamp time_step,
       jsonb_extract_path_text(points, 'Value', 'Numeric')::numeric numeric_value,
       jsonb_extract_path_text(points, 'Value', 'Display') display_value,
       notes,
       summary,
       num_points,
       time_range_start,
       time_range_end,
       response_time,
       response_version,
       location_identifier
  from (select jsonb_extract_path_text(json_content, 'UniqueId') time_series_unique_id,
               jsonb_array_elements(jsonb_extract_path(json_content, 'Points')) points,
               jsonb_extract_path(json_content, 'Notes') notes,
               jsonb_extract_path_text(json_content, 'Summary') summary,
               jsonb_extract_path_text(json_content, 'NumPoints')::numeric num_points,
               jsonb_extract_path_text(json_content, 'TimeRange', 'StartTime')::timestamp time_range_start,
               jsonb_extract_path_text(json_content, 'TimeRange', 'EndTime')::timestamp time_range_end,
               jsonb_extract_path_text(json_content, 'ResponseTime')::timestamp response_time,
               jsonb_extract_path_text(json_content, 'ResponseVersion')::numeric response_version,
               jsonb_extract_path_text(json_content, 'LocationIdentifier') location_identifier
          from json_data
         where json_data_id = ?) a

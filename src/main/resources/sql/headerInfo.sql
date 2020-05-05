insert
  into time_series_header_info (json_data_id,
                                time_series_unique_id,
                                notes,
                                summary,
                                num_points,
                                time_range_start,
                                time_range_end,
                                response_time,
                                response_version,
                                location_identifier
                               )
select json_data_id,
       time_series_unique_id,
       notes,
       summary,
       num_points,
       adjust_timestamp(time_range_start_text) time_range_start,
       adjust_timestamp(time_range_end_text) time_range_end,
       response_time,
       response_version,
       location_identifier
  from (select json_data_id,
               jsonb_extract_path_text(json_content, 'UniqueId') time_series_unique_id,
               jsonb_extract_path(json_content, 'Notes') notes,
               jsonb_extract_path_text(json_content, 'Summary') summary,
               jsonb_extract_path_text(json_content, 'NumPoints')::numeric num_points,
               jsonb_extract_path_text(json_content, 'TimeRange', 'StartTime') time_range_start_text,
               jsonb_extract_path_text(json_content, 'TimeRange', 'EndTime') time_range_end_text,
               jsonb_extract_path_text(json_content, 'ResponseTime')::timestamp response_time,
               jsonb_extract_path_text(json_content, 'ResponseVersion')::numeric response_version,
               jsonb_extract_path_text(json_content, 'LocationIdentifier') location_identifier
          from json_data
         where json_data_id = ?) a
 where time_series_unique_id is not null
returning time_series_unique_id

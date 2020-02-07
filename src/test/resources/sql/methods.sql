insert
  into time_series_methods (time_series_unique_id,
                            start_time,
                            end_time,
                            method_code
                           )
select time_series_unique_id,
       jsonb_extract_path_text(methods, 'StartTime')::timestamp start_time,
       jsonb_extract_path_text(methods, 'EndTime')::timestamp end_time,
       jsonb_extract_path_text(methods, 'MethodCode') method_code
  from (select jsonb_extract_path_text(json_content, 'UniqueId') time_series_unique_id,
               jsonb_array_elements(jsonb_extract_path(json_content, 'Methods')) methods
          from json_data
         where json_data_id = ?) a

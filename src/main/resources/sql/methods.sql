insert
  into time_series_methods (json_data_id,
                            start_time,
                            end_time,
                            method_code,
							partition_number
                           )
select json_data_id,
       adjust_timestamp(jsonb_extract_path_text(methods, 'StartTime')) start_time,
       adjust_timestamp(jsonb_extract_path_text(methods, 'EndTime')) end_time,
       jsonb_extract_path_text(methods, 'MethodCode') method_code,
	   partition_number
  from (select json_data_id,
               jsonb_array_elements(jsonb_extract_path(json_content, 'Methods')) methods,
               partition_number
          from json_data
         where json_data_id = ?
           and partition_number = ?) a

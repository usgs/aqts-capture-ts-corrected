insert
  into time_series_gap_tolerances (json_data_id,
                                   start_time,
                                   end_time,
                                   tolerance_in_minutes
                                  )
select json_data_id,
       adjust_timestamp(jsonb_extract_path_text(gap_tolerances, 'StartTime')) start_time,
       adjust_timestamp(jsonb_extract_path_text(gap_tolerances, 'EndTime')) end_time,
       jsonb_extract_path_text(gap_tolerances, 'ToleranceInMinutes')::numeric tolerance_in_minutes
  from (select json_data_id,
               jsonb_array_elements(jsonb_extract_path(json_content, 'GapTolerances')) gap_tolerances
          from json_data
         where json_data_id = ?) a

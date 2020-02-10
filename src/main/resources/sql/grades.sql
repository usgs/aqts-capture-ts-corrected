insert
  into time_series_grades (time_series_unique_id,
                           start_time,
                           end_time,
                           grade_code
                          )
select time_series_unique_id,
       jsonb_extract_path_text(grades, 'StartTime')::timestamp start_time,
       jsonb_extract_path_text(grades, 'EndTime')::timestamp end_time,
       jsonb_extract_path_text(grades, 'GradeCode') grade_code
  from (select jsonb_extract_path_text(json_content, 'UniqueId') time_series_unique_id,
               jsonb_array_elements(jsonb_extract_path(json_content, 'Grades')) grades
          from json_data
         where json_data_id = ?) a

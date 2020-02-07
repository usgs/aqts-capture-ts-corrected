select jsonb_extract_path_text(json_content, 'UniqueId') time_series_unique_id
  from json_data
 where json_data_id = ?
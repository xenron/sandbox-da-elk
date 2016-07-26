#!/usr/bin/env ./script/runner
puts "Loading relational database from Solr..."

MBARTISTS_SOLR_URL = 'http://localhost:8983/solr/mbartists'
BATCH_SIZE = 1500
MAX_RECORDS = nil   # the maximum number of records to load, or nil for all

solr_data = nil
offset = 0

# turn off acts_as_solr managing Artist lifecycle while we load data.
Artist.configuration[:offline] = true  

while true
  puts offset
  connection = Solr::Connection.new(MBARTISTS_SOLR_URL)
  solr_data = connection.send(Solr::Request::Standard.new({
    :query => '*:*',
    :rows=> BATCH_SIZE, 
    :start => offset, 
    :field_list =>['*','score']
  }))
  
  break if solr_data.hits.empty?  # at the end of the dataset available
  
  solr_data.hits.each do |doc|
    id = doc["id"]
    id = id[7..(id.length)]
    a = Artist.new(:name => doc["a_name"], :group_type => doc["a_type"], :release_date => doc["a_release_date_latest"])
    a.id = id
    begin
      a.save!
    rescue ActiveRecord::StatementInvalid => ar_si
      raise ar_si unless ar_si.to_s.include?("PRIMARY KEY must be unique") # sink duplicates
    end  
  end
  
  offset = offset + BATCH_SIZE
  
  unless MAX_RECORDS.nil?
    break if offset > MAX_RECORDS
  end
  
end

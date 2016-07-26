require 'rubygems'
require 'rbrainz'
include MusicBrainz


puts "Loading from MusicBrainz.org..."

BATCH_SIZE = 100
MAX_RECORDS = 10   # the maximum number of records to load, or nil for all
records_found = 0


query = MusicBrainz::Webservice::Query.new
('A' .. 'Z').each do |char|
  offset = 0
  puts "Loading letter #{char}"
  while true
    puts offset
    results = query.get_artists({:name => "#{char}*", :limit => BATCH_SIZE, :offset => offset})
  
  
    break if results.empty?  # at the end of the dataset available
  
    results.each do |doc|
      artist = doc.entity
      puts artist.name
      puts artist.tags.to_a.join("; ")
      releases = query.get_releases({:artistid => artist.id.uuid})
      releases.each do |release|
        puts "Release: #{release.entity.title}"
        puts "#{ MusicBrainz::Utils.get_language_name(release.entity.text_language)}/ #{MusicBrainz::Utils.get_script_name(release.entity.text_script)}"
        release.entity.types.each do |type|
          puts type.sub('http://musicbrainz.org/ns/mmd-1.0#',"")
        end
      end
      sleep 3
    end
  
    offset = offset + BATCH_SIZE
    records_found = records_found + results.size
    
    unless MAX_RECORDS.nil?
      break if records_found > MAX_RECORDS
    end
    sleep 3
  end
end
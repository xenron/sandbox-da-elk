require 'rubygems'
require 'rbrainz' # ruby gem for working with MusicBrainz data in Ruby
require 'block_mapper' # the generic mapper class


class BrainzMapper < BlockMapper
  
  def initialize()
    super
    before_each_source_item do |rec,index|
      # nothing to do
    end
    # remove ; / . , : and spaces from the end
    cleanup_regexp = /( |;|\/|\.|,|:)+$/
    after_each_mapped_value do |field,v|
      #puts "cleaning up #{field} value(s) before adding to solr..."
      if v.is_a?(String)
        v.gsub(cleanup_regexp, '') # clean this string and return
      elsif v.is_a?(Array)
        v.map{|vv|vv.gsub(cleanup_regexp, '')} # clean each value and return a new array
      else
        v # just return whatever it is
      end
    end
  end
  
  # pass in the query string, such as D* to find artists on MusicBrainz.org
  # a block can be used for logging etc..
  # 
  # mapper.from_brainz('Dave Matthews Band') do |mapped_doc|
  #   # do something here... logging etc..
  # end
  #
  # this returns an array of documents (hashes)
  #
  def from_brainz(query_string, shared_field_data={}, &blk)
    
    shared_field_data.each_pair do |k,v|
      # map each item in the hash to a solr field
      map k.to_sym, v
    end
    
    map :id do |rec,index|
      rec[:artist].id.uuid
    end
    
    map :title_t do |rec,index|
      rec[:artist].name
    end
    
    map :language_facet do |rec,index|
      rec[:releases].collect {|release| MusicBrainz::Utils.get_language_name(release.entity.text_language)}.compact.uniq unless rec[:releases].nil?
    end    
    
    map :script_facet do |rec,index|
      rec[:releases].collect {|release| MusicBrainz::Utils.get_script_name(release.entity.text_script)}.compact.uniq unless rec[:releases].nil?
    end    
    
    map :type_facet do |rec,index|
      types = []
      unless rec[:releases].nil?
        rec[:releases].each do |release|
          types << release.entity.types.collect{|type| type.sub('http://musicbrainz.org/ns/mmd-1.0#',"")}.compact
        end
      end
      types.flatten.uniq
    end    
    
    map :releases_t do |rec, index|
      rec[:releases].collect {|release|release.entity.title}.compact.uniq unless rec[:releases].nil?
    end
    
    records = create_records_from_music_brainz(query_string)
    
    self.run(records, &blk)
    
  end
  
  def create_records_from_music_brainz (query_string)
    records = []
    puts "Loading from MusicBrainz.org.  Query: #{query_string}"

    batch_size = 100
    max_records = 1000   # the maximum number of records to load, or nil for all
    records_found = 0
    sleep_time = 2  # Need to sleep between calls in order to not overload MusicBrainz.org


    query = MusicBrainz::Webservice::Query.new

    offset = 0
    while true
      puts "offset: #{offset}, records_found: #{records_found}"
      
      begin
        results = query.get_artists({:name => query_string, :limit => batch_size, :offset => offset})


        break if results.empty?  # at the end of the dataset available

        results.each do |doc|
          artist = doc.entity

          raise "Found someone with tags: #{artist.name}, we should index this data!" unless artist.tags.size == 0
          record = {}
          record[:artist] = artist
          begin
            record[:releases] = query.get_releases({:artistid => artist.id.uuid})
          rescue Exception => e  # sometimes we get back bad XML, so ignore it
            puts "Artist #{artist.name}, getting releases failed: #{e.class.name}"
          end
          records << record
          
          sleep sleep_time
        end

        offset = offset + batch_size
        records_found = records_found + results.size

        unless max_records.nil?
          break if records_found > max_records
        end
        sleep sleep_time
      rescue Exception => e  # if we time out, then put in a triple sleep and try again.
        puts "Had an exception: #{e}"
        sleep sleep_time * 3
      end
    end
        
    
    records
    
  end
  
end
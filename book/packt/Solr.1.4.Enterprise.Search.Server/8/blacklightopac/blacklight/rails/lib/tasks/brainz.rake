namespace :app do
  
  namespace :index do
    
    desc 'Index a Brainz using the lib/brainz_mapper class'
    task :brainz=>:environment do
      
      t = Time.now
      
      solr = Blacklight.solr
      
      mapper = BrainzMapper.new
      ('A' .. 'Z').each do |char|
        mapper.from_brainz("#{char}*", {:format_code_t => 'brainz'}) do |doc,index|
          puts "#{index} -- adding doc w/id : #{doc[:id]} to Solr"
          solr.add(doc)
        end
      
        puts "Sending commit to Solr..."
        solr.commit
      end
      puts "Complete."
      
      puts "Total Time: #{Time.now - t}"
      
    end
    
    
  end
  
end
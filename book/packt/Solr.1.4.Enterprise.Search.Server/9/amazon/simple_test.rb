#!/usr/bin/env ruby

# To run this test you many need to do:
#
# gem sources -a http://gems.github.com
# sudo gem install fastercsv
# sudo gem install mwmitchell-rsolr

#
# ruby simple_test.rb http://ec2-67-202-19-216.compute-1.amazonaws.com:8983/solr/mbreleases ../mb_releases.csv
#

require 'rubygems'
require 'faster_csv'
require 'rsolr'

if ARGV.empty? 
  puts "usage: ruby simple_test.rb <url> <csv_path>"
  puts " url similar to http://ec2-67-202-19-216.compute-1.amazonaws.com:8983/solr/mbreleases"
  puts " csv_path similar to ../mb_releases.csv"
  break
end

ARGV.each do|a|
  puts "Argument: #{a}"
end

BATCH_SIZE = 100  # how many documents to add at a time?  Try 1, 10, and 100.


url = ARGV[0]
csv_file_path = File.join(File.dirname(__FILE__), ARGV[1])

rsolr = RSolr.connect(:url => url)
rsolr.adapter.connector.adapter_name = :net_http

response = rsolr.select(:q=>'*:*')
rsolr.delete_by_query('*:*')
rsolr.commit

i = 0
documents = []
FasterCSV.foreach( csv_file_path, :headers           => true,
                                  :header_converters => :symbol) do |line|

                                    
  i += 1

  
  
  doc = {}
  
  #id,type,r_name,r_a_id,r_a_name,r_attributes,r_tracks,r_lang,r_event_country,r_event_date,r_event_date_earliest
  attributes_to_split = [:r_attributes, :r_event_country, :r_event_date]
  attributes = line.headers - attributes_to_split
  attributes.each { |attribute| doc[attribute] = line[attribute]}
  attributes_to_split.each { |attribute| doc[attribute] = line[attribute].split unless line[attribute].nil? }
  
  documents << doc
  

  rsolr.add(documents) && documents.clear if i % BATCH_SIZE == 0

#  rsolr.commit if i % 200 == 0   # uncomment to test committing more frequently
#  rsolr.optimize if i % 1000 == 0 # uncomment to try out different points for optimizing
  puts i if i % BATCH_SIZE == 0
end
rsolr.add(documents)
rsolr.commit



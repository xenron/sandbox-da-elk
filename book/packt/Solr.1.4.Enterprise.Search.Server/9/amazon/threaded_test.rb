#!/usr/bin/env ruby

# gem sources -a http://gems.github.com
# sudo gem install fastercsv
# sudo gem install mwmitchell-rsolr

#
# ruby threaded_test.rb 2000 4 http://ec2-72-44-43-64.compute-1.amazonaws.com:8983/solr/mbreleases
#

require 'rubygems'
require 'fastercsv'
require 'rsolr'
require 'pp'

if ARGV.empty? 
  puts "usage: ruby threaded_test.rb <threads> <url> <csv_path>"
  puts " threads: Number of threads such as 4"
  puts " url: similar to http://ec2-67-202-19-216.compute-1.amazonaws.com:8983/solr/mbreleases"
  puts " csv_path: similar to ../mb_releases.csv"
  break
end

ARGV.each do|a|
  puts "Argument: #{a}"
end

thread_count = ARGV[0].to_i
url = ARGV[1]
csv_file_path = File.join(File.dirname(__FILE__), ARGV[2])

line_count = open(csv_file_path).read.count("\n") 

batch_count = line_count / thread_count

data = FasterCSV.read(csv_file_path, :headers           => true,
                                :header_converters => :symbol)

rsolr = RSolr.connect(:url => url)
rsolr.adapter.connector.adapter_name = :net_http

response = rsolr.select(:q=>'*:*')
rsolr.delete_by_query('*:*')
rsolr.commit

threads = []
thread_count.times do |i|
  i = i + 1
  threads << Thread.new(i, data) { |myi, mydata|       
    
    starting_row = (myi * batch_count) - batch_count + 1
    ending_row = myi * batch_count
    puts "Thread #{myi}, #{starting_row}, #{ending_row}"
    
    myrsolr = RSolr.connect(:url => url)
    myrsolr.adapter.connector.adapter_name = :net_http
    row_counter = 0
    documents = []
    mydata.each do |line|
      row_counter += 1

      #puts "[#{myi}] (#{row_counter}) #{line[:r_name]}"
      
      if row_counter >= starting_row and row_counter <= ending_row
        doc = {}

        #id,type,r_name,r_a_id,r_a_name,r_attributes,r_tracks,r_lang,r_event_country,r_event_date,r_event_date_earliest
        attributes_to_split = [:r_attributes, :r_event_country, :r_event_date]
        attributes = line.headers - attributes_to_split
        attributes.each { |attribute| doc[attribute] = line[attribute]}
        attributes_to_split.each { |attribute| doc[attribute] = line[attribute].split unless line[attribute].nil? }
  
        documents << doc
 
      end
      if documents.size >= 500  # change this to a smaller number like 1 or 10 to see the performance difference
        myrsolr.add(documents) 
        documents = []
      end
      # rsolr.commit if row_counter % 200 == 0  # uncomment this to see impact of frequent commits!  Watch out for maxWarmingSearchers issue
      # rsolr.optimize unless row_counter.to_i%1000 != 0  #uncomment this to see impact of frequent optimizes
      puts "[#{myi}] #{row_counter}" if row_counter % 500 == 0
      
      
    end
    myrsolr.add(documents) unless documents.empty?
    myrsolr.commit
    puts "Ending Thread #{myi}"
  }
  
end

threads.each { |aThread|  aThread.join }



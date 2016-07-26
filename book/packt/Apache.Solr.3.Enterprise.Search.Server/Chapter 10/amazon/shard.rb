#!/usr/bin/env ruby

SHARDS = ['http://localhost:8983/solr/mbreleases', 
          'http://localhost:8984/solr/mbreleases',
          'http://localhost:8985/solr/mbreleases']
docs_ids = (1...200).to_a
shard_count = []
doc_ids.each do |doc_id|
  shard = doc_id % SHARDS.size
  shard_count << shard
  puts "index document #{doc_id} into shard #{shard}"
end

puts shard_count.count(0)
puts shard_count.count(1)
puts shard_count.count(2)
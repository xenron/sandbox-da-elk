class Artist < ActiveRecord::Base
  acts_as_solr :fields => [:name, :group_type, :release_date]
end

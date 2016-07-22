# Adds a Currency Symbol to the field
#
#filter {
#    currency{
#        name => "$"
#    }
#}

require "logstash/filters/base"
require "logstash/namespace"

class LogStash::Filters::Currency < LogStash::Filters::Base

config_name "currency"

config :name, :validate => :string, :default => "$"

public
def register
#do nothing
end

public
def filter(event)
    if @name
        msg = @name + event["message"]
        event["message"] = msg
    end
end

end
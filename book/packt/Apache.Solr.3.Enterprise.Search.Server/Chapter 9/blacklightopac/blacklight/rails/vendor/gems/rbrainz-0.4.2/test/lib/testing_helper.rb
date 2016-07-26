# $Id: testing_helper.rb 203 2008-03-17 11:00:15Z phw $
#
# Usefull helper methods which are used in different test classes.
#
# Author::    Philipp Wolfer (mailto:phw@rubyforge.org)
# Copyright:: Copyright (c) 2007, Philipp Wolfer
# License::   RBrainz is free software distributed under a BSD style license.
#             See LICENSE[file:../LICENSE.html] for permissions.

# Converts a query string into a hash.
def query_string_to_hash query_string
  Hash[*query_string.scan(/(.+?)=(.*?)(?:&|$)/).flatten].each_value {|v|
    v.gsub!('+', ' ')
    v.gsub!(/%([0-9a-f]{2})/i) { [$1.hex].pack 'C' }
  }
end
class Artist < ActiveRecord::Migration
  def self.up
    add_column "artists", "image_url", :string
  end

  def self.down
    add_column "artists", "image_url"
  end
end

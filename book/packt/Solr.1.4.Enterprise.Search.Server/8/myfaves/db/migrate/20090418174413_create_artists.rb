class CreateArtists < ActiveRecord::Migration
  def self.up
    create_table :artists do |t|
      t.string :name
      t.string :group_type
      t.datetime :release_date

      t.timestamps
    end
  end

  def self.down
    drop_table :artists
  end
end

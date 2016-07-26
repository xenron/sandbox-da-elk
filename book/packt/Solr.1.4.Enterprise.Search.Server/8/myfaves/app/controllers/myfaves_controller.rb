class MyfavesController < ApplicationController
  before_filter :myfaves
  def index

  end

  def create
    @artist = Artist.find(params[:artist][:id])
    if @artist.nil?
      flash[:notice] = "Artist matching #{params[:artist][:name]} was not found."
    else
      if @artist.image_url.nil?
        @artist.image_url = find_artist_image(@artist.name)
        @artist.save!
      end
      @myfaves << @artist
      @myfaves.uniq!
    end
    redirect_to :action => :index
    
  end

  def destroy
    @artist = Artist.find(params[:id])

    @myfaves.delete @artist

    redirect_to :action => :index
  end
  
protected
  def myfaves
    unless session[:myfaves]
      session[:myfaves] = []
    end
    @myfaves = session[:myfaves]    
  end
  
  def find_artist_image(name)
    require 'uri'
    name = URI.escape(name)
    resp = Net::HTTP.get_response(URI.parse("http://search.yahooapis.com/ImageSearchService/V1/imageSearch?appid=YahooDemo&query=#{name}&results=1&output=json"))
    result = ActiveSupport::JSON.decode(resp.body)    
    
    unless result["ResultSet"]["totalResultsReturned"] == 0
      return result["ResultSet"]["Result"].first["Thumbnail"]["Url"]
    end
    nil
  end
end

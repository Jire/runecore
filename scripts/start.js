
	function configXStream(config) {
		config.xStream.createParser()
		config.xStream.setupParser()
		config.logger.info('Configured xStream sucessfully!') 
	}
	
	function readWorldList(config) {
		config.logger.info('Loading world list...')
		config.getWorldListBuilder().worldList = config.xStream.getxStreamInstance().fromXML(new java.io.FileInputStream("./cfg/worlds.XML"));
		config.getWorldListBuilder().buildWorlds()
		config.logger.info('Configured ' +config.getWorldListBuilder().worldList.size()+ ' world(s)') 
	}
	
	function buildRegionData(config, constants, builder, reader) {
		if(!constants.MAPDATA_FILE.exists()) {
			builder.pack('data/mapdata/', 'data/mapdata.dat')
		}
		reader.load(config.mapData)
	}
	
	function loadPlugins(plugin) {
		plugin.loadInternalPlugins()
		plugin.startLoops()
	}
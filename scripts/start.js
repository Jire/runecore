var start = this

	function configXStream(config) {
		config.xStream.createParser()
		config.xStream.setupParser()
		config.logger.info('Configured xStream sucessfully!') 
	}
	
	function readWorldList(config) {
		config.logger.info('Loading world list...')
		config.setWorldList()
		config.logger.info('Loaded '+config.world.size()+' world(s)') 
	}